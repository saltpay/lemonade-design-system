/**
 * Classifies a unified diff of BCV-generated `.api` / `.klib.api` files into:
 *
 *  - [Verdict.NoChanges]     — no public API delta. CI silently passes.
 *  - [Verdict.AdditionsOnly] — purely additive ABI changes. CI auto-passes.
 *  - [Verdict.Breaking]      — ABI-incompatible changes. CI requires a named
 *                              maintainer to approve the PR.
 *
 * Two ABI breakage rules are applied to the diff:
 *
 *  1. Any `-` line (excluding `---` file headers) — a declaration was removed,
 *     renamed, had its signature changed, or had its visibility narrowed.
 *
 *  2. A `+` line declaring an abstract member (`abstract fun|val|var`) inside
 *     a class/interface block whose declaration line is **not** also a `+`
 *     line — i.e. an abstract member added to a type that already existed.
 *     This case shows up as purely additive in the diff but causes
 *     `AbstractMethodError` at runtime for any pre-existing implementer.
 *
 * Brand-new types containing abstract members produce only `+` lines (their
 * class header is also `+`) and are therefore correctly treated as additive
 * — there are no pre-existing implementers to break.
 *
 * Expected input: `git diff -U999999 …` so each hunk carries the full file's
 * structure and the enclosing class declaration is always visible to the
 * state machine.
 */
public object ApiStabilityClassifier {

    public fun classify(diff: String): Verdict {
        if (diff.isBlank()) return Verdict.NoChanges

        val reasons = mutableListOf<String>()

        val removed = collectRemovedDeclarations(diff)
        if (removed.isNotEmpty()) {
            reasons += "removed/changed declaration(s): ${removed.summarize()}"
        }

        val abstractAdded = collectAbstractMembersAddedToExistingTypes(diff)
        if (abstractAdded.isNotEmpty()) {
            reasons += "new abstract member(s) on existing type: ${abstractAdded.summarize()}"
        }

        return if (reasons.isEmpty()) Verdict.AdditionsOnly else Verdict.Breaking(reasons)
    }

    private fun collectRemovedDeclarations(diff: String): List<String> =
        diff.lineSequence()
            .filter { it.startsWith("-") && !it.startsWith("---") }
            .map { it.removePrefix("-").trim() }
            .filter { it.isNotEmpty() }
            .toList()

    private fun collectAbstractMembersAddedToExistingTypes(diff: String): List<String> {
        val results = mutableListOf<String>()
        var enclosingTypeIsNew: Boolean? = null
        var sawFileHeader = false

        for (rawLine in diff.lineSequence()) {
            when {
                rawLine.startsWith("diff --git") || rawLine.startsWith("index ") -> {
                    enclosingTypeIsNew = null
                    sawFileHeader = false
                }
                rawLine.startsWith("---") || rawLine.startsWith("+++") -> {
                    sawFileHeader = true
                }
                rawLine.startsWith("@@") -> {
                    enclosingTypeIsNew = null
                }
                else -> {
                    if (!sawFileHeader) continue
                    val (prefix, body) = rawLine.takeIfDiffLine() ?: continue

                    val typeAddition = body.matchTypeDeclaration()
                    if (typeAddition != null) {
                        enclosingTypeIsNew = (prefix == '+')
                        continue
                    }

                    if (prefix == '+') {
                        val abstractName = body.matchAbstractMember()
                        if (abstractName != null && enclosingTypeIsNew == false) {
                            results += abstractName
                        }
                    }
                }
            }
        }
        return results
    }

    private fun String.takeIfDiffLine(): Pair<Char, String>? {
        if (isEmpty()) return null
        val c = this[0]
        return if (c == '+' || c == '-' || c == ' ') c to substring(1) else null
    }

    /**
     * Returns a non-null token if the body looks like a class/interface declaration in either
     * the JVM `.api` format (`public abstract interface class com/foo/Bar {`) or the klib format
     * (`abstract interface com.foo/Bar { …`).
     */
    private fun String.matchTypeDeclaration(): String? {
        val trimmed = trim()
        return jvmTypeDeclRegex.find(trimmed)?.value
            ?: klibTypeDeclRegex.find(trimmed)?.value
    }

    /**
     * Returns the declared member name if the body is an abstract `fun` / `val` / `var`
     * declaration in either format. Returns null otherwise (concrete methods, fields, etc).
     *
     * The klib format also uses `open fun` for default interface methods — those are NOT
     * abstract and must not match.
     */
    private fun String.matchAbstractMember(): String? {
        val trimmed = trim()
        return jvmAbstractMemberRegex.find(trimmed)?.groupValues?.get(1)
            ?: klibAbstractMemberRegex.find(trimmed)?.groupValues?.get(1)
    }

    private val jvmTypeDeclRegex = Regex(
        """^(?:public|protected|private)\s+(?:final\s+|abstract\s+|static\s+)*(?:interface|class|enum)\s+"""
    )

    private val klibTypeDeclRegex = Regex(
        """^(?:final\s+|abstract\s+|sealed\s+|open\s+|enum\s+)*(?:interface|class|object)\s+"""
    )

    private val jvmAbstractMemberRegex = Regex(
        """^public\s+abstract\s+(?:fun|field)\s+(\w+)"""
    )

    private val klibAbstractMemberRegex = Regex(
        """^abstract\s+(?:fun|val|var)\s+(\w+)"""
    )

    private fun List<String>.summarize(limit: Int = 5): String {
        if (size <= limit) return joinToString("; ")
        val head = take(limit).joinToString("; ")
        return "$head; … (${size - limit} more)"
    }
}

public sealed interface Verdict {
    public object NoChanges : Verdict
    public object AdditionsOnly : Verdict
    public data class Breaking(val reasons: List<String>) : Verdict
}

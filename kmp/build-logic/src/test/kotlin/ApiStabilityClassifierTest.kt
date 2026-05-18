import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import java.io.File

/**
 * Each fixture is a real unified diff produced by running BCV's `apiDump` task
 * twice — once with a "before" source file, once with an "after" — and capturing
 * `diff -u -U999999` between the two baseline snapshots. See
 * `kmp/scripts/capture-all-fixtures.sh` for the source pairs used.
 *
 * Tests are parameterized over the three bucket directories under
 * `src/test/resources/fixtures/`:
 *  - `additions-only/` → must classify as [Verdict.AdditionsOnly]
 *  - `breaking/`       → must classify as [Verdict.Breaking]
 *  - `no-changes/`     → must classify as [Verdict.NoChanges]
 */
class ApiStabilityClassifierTest {

    @TestFactory
    fun `additions-only fixtures classify as AdditionsOnly`(): List<DynamicTest> =
        loadBucket("additions-only").map { fixture ->
            DynamicTest.dynamicTest(fixture.name) {
                val verdict = ApiStabilityClassifier.classify(fixture.diff)
                assertEquals(
                    Verdict.AdditionsOnly,
                    verdict,
                    "Expected ${fixture.name} to classify as AdditionsOnly but got $verdict",
                )
            }
        }

    @TestFactory
    fun `breaking fixtures classify as Breaking`(): List<DynamicTest> =
        loadBucket("breaking").map { fixture ->
            DynamicTest.dynamicTest(fixture.name) {
                val verdict = ApiStabilityClassifier.classify(fixture.diff)
                assertIs<Verdict.Breaking>(
                    verdict,
                    "Expected ${fixture.name} to classify as Breaking but got $verdict",
                )
                assertTrue(
                    verdict.reasons.isNotEmpty(),
                    "Breaking verdict for ${fixture.name} produced no reasons",
                )
            }
        }

    @TestFactory
    fun `no-changes fixtures classify as NoChanges`(): List<DynamicTest> =
        loadBucket("no-changes").map { fixture ->
            DynamicTest.dynamicTest(fixture.name) {
                val verdict = ApiStabilityClassifier.classify(fixture.diff)
                assertEquals(
                    Verdict.NoChanges,
                    verdict,
                    "Expected ${fixture.name} to classify as NoChanges but got $verdict",
                )
            }
        }

    /** Targeted assertions on specific reasons — guards against the easy-to-regress cases. */

    @Test
    fun `abstract member added to existing interface mentions abstract in reasons`() {
        val fixture = loadFixture("breaking/11-add-abstract-to-existing-iface.diff")
        val verdict = ApiStabilityClassifier.classify(fixture)
        assertIs<Verdict.Breaking>(verdict)
        assertTrue(
            verdict.reasons.any { it.contains("abstract", ignoreCase = true) },
            "Expected an 'abstract member' reason. Reasons: ${verdict.reasons}",
        )
    }

    @Test
    fun `abstract member added to existing abstract class mentions abstract in reasons`() {
        val fixture = loadFixture("breaking/12-add-abstract-to-existing-class.diff")
        val verdict = ApiStabilityClassifier.classify(fixture)
        assertIs<Verdict.Breaking>(verdict)
        assertTrue(
            verdict.reasons.any { it.contains("abstract", ignoreCase = true) },
            "Expected an 'abstract member' reason. Reasons: ${verdict.reasons}",
        )
    }

    @Test
    fun `removed function mentions removed in reasons`() {
        val fixture = loadFixture("breaking/07-remove-fun.diff")
        val verdict = ApiStabilityClassifier.classify(fixture)
        assertIs<Verdict.Breaking>(verdict)
        assertTrue(
            verdict.reasons.any { it.contains("removed", ignoreCase = true) },
            "Expected a 'removed' reason. Reasons: ${verdict.reasons}",
        )
    }

    @Test
    fun `default-parameter addition is classified breaking because JVM signature changes`() {
        val fixture = loadFixture("breaking/08-add-default-param.diff")
        val verdict = ApiStabilityClassifier.classify(fixture)
        assertIs<Verdict.Breaking>(verdict)
    }

    @Test
    fun `brand-new interface with abstract members is additions-only despite abstract keyword`() {
        val fixture = loadFixture("additions-only/05-brand-new-iface-with-abstracts.diff")
        val verdict = ApiStabilityClassifier.classify(fixture)
        assertEquals(Verdict.AdditionsOnly, verdict)
    }

    @Test
    fun `default-body method on existing interface is additions-only (open or non-abstract)`() {
        val fixture = loadFixture("additions-only/06-add-default-method-existing-iface.diff")
        val verdict = ApiStabilityClassifier.classify(fixture)
        assertEquals(Verdict.AdditionsOnly, verdict)
    }

    @Test
    fun `empty input is NoChanges`() {
        assertEquals(Verdict.NoChanges, ApiStabilityClassifier.classify(""))
        assertEquals(Verdict.NoChanges, ApiStabilityClassifier.classify("   \n  \n"))
    }

    // --- helpers ---

    private data class Fixture(val name: String, val diff: String)

    private fun loadBucket(bucket: String): List<Fixture> {
        val dir = fixtureDir().resolve(bucket)
        check(dir.isDirectory) { "Fixture directory missing: $dir" }
        return dir.listFiles { _, name -> name.endsWith(".diff") }
            .orEmpty()
            .sortedBy { it.name }
            .map { Fixture(name = it.name, diff = it.readText()) }
            .also {
                check(it.isNotEmpty()) { "No .diff fixtures in $dir" }
            }
    }

    private fun loadFixture(relative: String): String =
        fixtureDir().resolve(relative).readText()

    private fun fixtureDir(): File {
        val resource = checkNotNull(this::class.java.classLoader.getResource("fixtures")) {
            "fixtures resource directory not found on test classpath"
        }
        return File(resource.toURI())
    }
}

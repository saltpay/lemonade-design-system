#!/usr/bin/env bash
# Drives capture-bcv-fixture.sh through every scenario.
# Each scenario provides two source-code states. We capture the diff between
# their apiDump baselines so the classifier's tests run against real BCV output.

set -euo pipefail
cd "$(dirname "$0")/.."

STAGES=scripts/fixture-stages

mkdir -p "$STAGES"

# ----- additions-only fixtures (classifier must auto-pass) -----

cat > "$STAGES/01a.kt" <<'EOF'
package com.teya.lemonade.core
EOF
cat > "$STAGES/01b.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvNewTopLevelFun(): String = "hi"
EOF
FIXTURE_BUCKET=additions-only ./scripts/capture-bcv-fixture.sh 01-pure-add-toplevel-fun "$STAGES/01a.kt" "$STAGES/01b.kt"

cat > "$STAGES/02a.kt" <<'EOF'
package com.teya.lemonade.core
EOF
cat > "$STAGES/02b.kt" <<'EOF'
package com.teya.lemonade.core
public class BcvNewPlainClass(public val name: String)
EOF
FIXTURE_BUCKET=additions-only ./scripts/capture-bcv-fixture.sh 02-pure-add-class "$STAGES/02a.kt" "$STAGES/02b.kt"

cat > "$STAGES/03a.kt" <<'EOF'
package com.teya.lemonade.core
public enum class BcvEnumThree { First, Second }
EOF
cat > "$STAGES/03b.kt" <<'EOF'
package com.teya.lemonade.core
public enum class BcvEnumThree { First, Second, Third }
EOF
FIXTURE_BUCKET=additions-only ./scripts/capture-bcv-fixture.sh 03-add-enum-entry "$STAGES/03a.kt" "$STAGES/03b.kt"

cat > "$STAGES/04a.kt" <<'EOF'
package com.teya.lemonade.core
public sealed class BcvSealedA {
    public object First : BcvSealedA()
}
EOF
cat > "$STAGES/04b.kt" <<'EOF'
package com.teya.lemonade.core
public sealed class BcvSealedA {
    public object First : BcvSealedA()
    public object Second : BcvSealedA()
}
EOF
FIXTURE_BUCKET=additions-only ./scripts/capture-bcv-fixture.sh 04-add-sealed-subclass "$STAGES/04a.kt" "$STAGES/04b.kt"

cat > "$STAGES/05a.kt" <<'EOF'
package com.teya.lemonade.core
EOF
cat > "$STAGES/05b.kt" <<'EOF'
package com.teya.lemonade.core
public interface BcvBrandNewIface {
    public fun foo(): String
    public fun bar(): Int
}
EOF
FIXTURE_BUCKET=additions-only ./scripts/capture-bcv-fixture.sh 05-brand-new-iface-with-abstracts "$STAGES/05a.kt" "$STAGES/05b.kt"

cat > "$STAGES/06a.kt" <<'EOF'
package com.teya.lemonade.core
public interface BcvIfaceWithDefault {
    public fun existing(): String
}
EOF
cat > "$STAGES/06b.kt" <<'EOF'
package com.teya.lemonade.core
public interface BcvIfaceWithDefault {
    public fun existing(): String
    public fun newDefault(): String { return "default" }
}
EOF
FIXTURE_BUCKET=additions-only ./scripts/capture-bcv-fixture.sh 06-add-default-method-existing-iface "$STAGES/06a.kt" "$STAGES/06b.kt"

# ----- breaking fixtures (classifier must require approval) -----

cat > "$STAGES/07a.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvRemoveMe(): String = "bye"
public fun bcvKeepMe(): String = "hi"
EOF
cat > "$STAGES/07b.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvKeepMe(): String = "hi"
EOF
FIXTURE_BUCKET=breaking ./scripts/capture-bcv-fixture.sh 07-remove-fun "$STAGES/07a.kt" "$STAGES/07b.kt"

cat > "$STAGES/08a.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvDefaultParam(name: String): String = "hi $name"
EOF
cat > "$STAGES/08b.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvDefaultParam(name: String, greeting: String = "hi"): String = "$greeting $name"
EOF
FIXTURE_BUCKET=breaking ./scripts/capture-bcv-fixture.sh 08-add-default-param "$STAGES/08a.kt" "$STAGES/08b.kt"

cat > "$STAGES/09a.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvReturnType(): String = "0"
EOF
cat > "$STAGES/09b.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvReturnType(): Int = 0
EOF
FIXTURE_BUCKET=breaking ./scripts/capture-bcv-fixture.sh 09-change-return-type "$STAGES/09a.kt" "$STAGES/09b.kt"

cat > "$STAGES/10a.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvVisibilityNarrow(): String = "hi"
EOF
cat > "$STAGES/10b.kt" <<'EOF'
package com.teya.lemonade.core
internal fun bcvVisibilityNarrow(): String = "hi"
EOF
FIXTURE_BUCKET=breaking ./scripts/capture-bcv-fixture.sh 10-narrow-visibility "$STAGES/10a.kt" "$STAGES/10b.kt"

cat > "$STAGES/11a.kt" <<'EOF'
package com.teya.lemonade.core
public interface BcvExistingIface {
    public fun existingMethod(): String
}
EOF
cat > "$STAGES/11b.kt" <<'EOF'
package com.teya.lemonade.core
public interface BcvExistingIface {
    public fun existingMethod(): String
    public fun newAbstractMethod(): Int
}
EOF
FIXTURE_BUCKET=breaking ./scripts/capture-bcv-fixture.sh 11-add-abstract-to-existing-iface "$STAGES/11a.kt" "$STAGES/11b.kt"

cat > "$STAGES/12a.kt" <<'EOF'
package com.teya.lemonade.core
public abstract class BcvExistingAbsClass {
    public abstract fun existingAbs(): String
}
EOF
cat > "$STAGES/12b.kt" <<'EOF'
package com.teya.lemonade.core
public abstract class BcvExistingAbsClass {
    public abstract fun existingAbs(): String
    public abstract fun newAbs(): Int
}
EOF
FIXTURE_BUCKET=breaking ./scripts/capture-bcv-fixture.sh 12-add-abstract-to-existing-class "$STAGES/12a.kt" "$STAGES/12b.kt"

# ----- no-changes (classifier returns NoChanges, never asks for approval) -----

cat > "$STAGES/13a.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvUnchanged(): String = "v1"
EOF
cat > "$STAGES/13b.kt" <<'EOF'
package com.teya.lemonade.core
public fun bcvUnchanged(): String = "v2"
EOF
FIXTURE_BUCKET=no-changes ./scripts/capture-bcv-fixture.sh 13-impl-only-change "$STAGES/13a.kt" "$STAGES/13b.kt"

rm -rf "$STAGES"
echo
echo "All fixtures captured:"
find build-logic/src/test/resources/fixtures -name "*.diff" | sort

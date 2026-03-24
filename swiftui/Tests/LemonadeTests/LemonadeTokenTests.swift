import XCTest
@testable import Lemonade

final class LemonadeTokenTests: XCTestCase {
    func testSpacingTokensHaveExpectedValues() {
        XCTAssertEqual(LemonadeTheme.spaces.spacing100, 4)
        XCTAssertEqual(LemonadeTheme.spaces.spacing200, 8)
        XCTAssertEqual(LemonadeTheme.spaces.spacing300, 12)
    }

    func testRadiusTokensHaveExpectedValues() {
        XCTAssertEqual(LemonadeTheme.radius.radius100, 4)
        XCTAssertEqual(LemonadeTheme.radius.radius300, 12)
    }

    func testSizeTokensHaveExpectedValues() {
        XCTAssertEqual(LemonadeTheme.sizes.size1600, 64)
        XCTAssertEqual(LemonadeTheme.sizes.size1800, 72)
        XCTAssertEqual(LemonadeTheme.sizes.size2000, 80)
        XCTAssertEqual(LemonadeTheme.sizes.size2200, 88)
        XCTAssertEqual(LemonadeTheme.sizes.size2400, 96)
        XCTAssertEqual(LemonadeTheme.sizes.size2500, 100)
    }

    func testTypographyFontFamily() {
        XCTAssertEqual(LemonadeTypography.fontFamily, "Figtree")
    }
}

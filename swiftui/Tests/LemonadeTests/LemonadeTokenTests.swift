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

    func testTypographyFontFamily() {
        XCTAssertEqual(LemonadeTypography.fontFamily, "Figtree")
    }
}

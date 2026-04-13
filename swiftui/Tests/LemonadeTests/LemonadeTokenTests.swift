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

    func testShadowCaseIterableOrder() {
        let expected: [LemonadeShadow] = [.xsmall, .small, .medium, .large, .xlarge, .none]
        XCTAssertEqual(LemonadeShadow.allCases, expected)
    }

    func testShadowLayerCounts() {
        XCTAssertEqual(LemonadeShadow.xsmall.shadowLayers.count, 1)
        XCTAssertEqual(LemonadeShadow.small.shadowLayers.count, 2)
        XCTAssertEqual(LemonadeShadow.medium.shadowLayers.count, 2)
        XCTAssertEqual(LemonadeShadow.large.shadowLayers.count, 2)
        XCTAssertEqual(LemonadeShadow.xlarge.shadowLayers.count, 2)
        XCTAssertEqual(LemonadeShadow.none.shadowLayers.count, 1)
    }

    func testShadowLayerValues() {
        let xsmall = LemonadeShadow.xsmall.shadowLayers[0]
        XCTAssertEqual(xsmall.blur, 2)
        XCTAssertEqual(xsmall.spread, 0)
        XCTAssertEqual(xsmall.offsetX, 0)
        XCTAssertEqual(xsmall.offsetY, 1)

        let xlarge0 = LemonadeShadow.xlarge.shadowLayers[0]
        XCTAssertEqual(xlarge0.blur, 10)
        XCTAssertEqual(xlarge0.spread, -6)
        XCTAssertEqual(xlarge0.offsetX, 0)
        XCTAssertEqual(xlarge0.offsetY, 8)

        let xlarge1 = LemonadeShadow.xlarge.shadowLayers[1]
        XCTAssertEqual(xlarge1.blur, 25)
        XCTAssertEqual(xlarge1.spread, -5)
        XCTAssertEqual(xlarge1.offsetX, 0)
        XCTAssertEqual(xlarge1.offsetY, 20)
    }
}

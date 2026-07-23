import XCTest
@testable import Lemonade

final class LemonadeTooltipPositioningTests: XCTestCase {

    /// A 393x852 iPhone container, matching the design frames.
    private let container = CGSize(width: 393, height: 852)

    /// The store selector, in the top half of the screen.
    private let topAnchor = CGRect(x: 142, y: 185, width: 109, height: 36)

    /// The payment-method chip, in the bottom half of the screen.
    private let bottomAnchor = CGRect(x: 123, y: 461, width: 146, height: 36)

    func testAutoPlacementPutsTheTooltipBelowAnAnchorInTheTopHalf() {
        XCTAssertTrue(
            LemonadeTooltipPositioning.pointsUp(
                anchor: topAnchor,
                containerSize: container,
                forcedPlacement: nil
            )
        )
    }

    func testAutoPlacementPutsTheTooltipAboveAnAnchorInTheBottomHalf() {
        XCTAssertFalse(
            LemonadeTooltipPositioning.pointsUp(
                anchor: bottomAnchor,
                containerSize: container,
                forcedPlacement: nil
            )
        )
    }

    func testForcedBottomPlacementPutsTheTooltipAboveAnAnchorInTheTopHalf() {
        XCTAssertFalse(
            LemonadeTooltipPositioning.pointsUp(
                anchor: topAnchor,
                containerSize: container,
                forcedPlacement: .bottomCenter
            )
        )
    }

    func testForcedTopPlacementPutsTheTooltipBelowAnAnchorInTheBottomHalf() {
        XCTAssertTrue(
            LemonadeTooltipPositioning.pointsUp(
                anchor: bottomAnchor,
                containerSize: container,
                forcedPlacement: .topCenter
            )
        )
    }

    func testAnIndicatorlessPlacementFallsBackToAutoPlacement() {
        // `LemonadeTooltipIndicatorPlacement.none` must be spelled out: a bare `.none` in an
        // Optional parameter resolves to `Optional.none`, which is a different test.
        XCTAssertTrue(
            LemonadeTooltipPositioning.pointsUp(
                anchor: topAnchor,
                containerSize: container,
                forcedPlacement: LemonadeTooltipIndicatorPlacement.none
            )
        )
    }
}

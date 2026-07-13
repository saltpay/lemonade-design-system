import Foundation
import XCTest
@testable import Lemonade

final class LemonadeTextInputTransformationTests: XCTestCase {

    // MARK: - Minimal replacement

    // A whole-buffer rewrite would make one keystroke undo the entire field, and it is what leaves
    // a rejected paste sitting on the undo stack. The narrower the replacement, the less of the
    // field's own undo history an override disturbs.

    func testInsertingACharacterReplacesNothing() {
        let edit = lemonadeMinimalReplacement(from: "1234", to: "12X34")

        XCTAssertEqual(edit.range, NSRange(location: 2, length: 0))
        XCTAssertEqual(edit.replacement, "X")
    }

    func testDeletingACharacterInsertsNothing() {
        let edit = lemonadeMinimalReplacement(from: "12X34", to: "1234")

        XCTAssertEqual(edit.range, NSRange(location: 2, length: 1))
        XCTAssertEqual(edit.replacement, "")
    }

    func testAnUnchangedStringIsAnEmptyEdit() {
        let edit = lemonadeMinimalReplacement(from: "1234", to: "1234")

        XCTAssertEqual(edit.range.length, 0)
        XCTAssertEqual(edit.replacement, "")
    }

    func testReformattingTouchesOnlyTheSpanThatMoved() {
        // A sort code gaining its second separator: "12-345" -> "12-34-5".
        let edit = lemonadeMinimalReplacement(from: "12-345", to: "12-34-5")

        XCTAssertEqual(edit.range, NSRange(location: 5, length: 0))
        XCTAssertEqual(edit.replacement, "-")
    }

    func testReplacingTheWholeStringIsStillCorrect() {
        let edit = lemonadeMinimalReplacement(from: "abc", to: "xyz")

        XCTAssertEqual(edit.range, NSRange(location: 0, length: 3))
        XCTAssertEqual(edit.replacement, "xyz")
    }

    func testOffsetsAreUTF16SoTheyLineUpWithNSRange() {
        // "é" is one Character but the emoji is two UTF-16 code units. UIKit counts code units, so
        // an edit computed in Characters would land in the wrong place.
        let edit = lemonadeMinimalReplacement(from: "é🇬🇧", to: "é🇬🇧!")

        XCTAssertEqual(edit.range, NSRange(location: "é🇬🇧".utf16.count, length: 0))
        XCTAssertEqual(edit.replacement, "!")
    }

    // MARK: - Edit result

    func testTheCaretIsClampedIntoTheText() {
        XCTAssertEqual(LemonadeTextEditResult(text: "123", cursorPosition: 99).cursorPosition, 3)
        XCTAssertEqual(LemonadeTextEditResult(text: "123", cursorPosition: -1).cursorPosition, 0)
    }
}

package io.bashpsk.zerodownload.domain.extension

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

/**
 * Converts a top-left offset to a center offset based on the given size.
 *
 * This function assumes the current `Offset` represents the top-left corner of a rectangle.
 * It calculates the center of that rectangle by adding half of the `size.width` to the x-coordinate
 * and half of the `size.height` to the y-coordinate.
 *
 * @param size The size of the rectangle.
 * @return A new `Offset` representing the center of the rectangle.
 */
internal fun Offset.toCenter(size: Size): Offset {

    return Offset(x = x + size.width / 2, y = y + size.height / 2)
}

/**
 * Calculates the top-left offset of a rectangle given its center offset and size.
 *
 * This function assumes that the receiver `Offset` represents the center point of the rectangle.
 * It then subtracts half of the width and half of the height from the center's x and y coordinates,
 * respectively, to determine the coordinates of the top-left corner.
 *
 * @param size The size (width and height) of the rectangle.
 * @return An `Offset` representing the top-left corner of the rectangle.
 */
internal fun Offset.toTopLeft(size: Size): Offset {

    return Offset(x = x - size.width / 2, y = y - size.height / 2)
}

/**
 * Calculates the top-right corner of a rectangle, given its top-left corner (this offset) and its
 * size.
 *
 * @param size The size of the rectangle.
 * @return The offset representing the top-right corner of the rectangle.
 */
internal fun Offset.toTopRight(size: Size): Offset {

    return Offset(x = x + size.width, y = y)
}

/**
 * Calculates the bottom-left corner offset of a rectangle given its top-left corner and size.
 *
 * This function assumes the current `Offset` represents the top-left corner of a rectangle.
 * It then calculates the coordinates of the bottom-left corner of that rectangle.
 *
 * @param size The `Size` of the rectangle (width and height).
 * @return An `Offset` representing the bottom-left corner of the rectangle.
 * The x-coordinate remains the same as the original offset.
 * The y-coordinate is the original y-coordinate plus the height of the rectangle.
 */
internal fun Offset.toBottomLeft(size: Size): Offset {

    return Offset(x = x, y = y + size.height)
}

/**
 * Calculates the bottom-right corner of a rectangle, given its top-left corner (this offset)
 * and its size.
 *
 * @param size The size (width and height) of the rectangle.
 * @return An [Offset] representing the coordinates of the bottom-right corner.
 */
internal fun Offset.toBottomRight(size: Size): Offset {

    return Offset(x = x + size.width, y = y + size.height)
}

/**
 * Converts this offset to represent the top-center point of a rectangle
 * with the given size, assuming this offset is the top-left corner of that rectangle.
 *
 * @param size The size of the rectangle.
 * @return A new `Offset` representing the top-center point.
 */
internal fun Offset.toTopCenter(size: Size): Offset {

    return Offset(x = x + size.width / 2, y = y)
}

/**
 * Calculates the bottom center coordinates of a rectangle given its top-left corner and size.
 *
 * This function assumes the current `Offset` represents the top-left corner of the rectangle.
 *
 * @param size The `Size` of the rectangle (width and height).
 * @return An `Offset` representing the coordinates of the bottom center of the rectangle.
 */
internal fun Offset.toBottomCenter(size: Size): Offset {

    return Offset(x = x + size.width / 2, y = y + size.height)
}

/**
 * Adjusts this offset to the left-center position relative to a given size.
 *
 * This function is typically used to position an item such that its left edge aligns with this
 * offset's x-coordinate, and its vertical center aligns with this offset's y-coordinate plus half
 * of the item's height.
 *
 * @param size The size of the item for which the left-center position is being calculated.
 * @return A new `Offset` representing the left-center position. The x-coordinate remains the same
 * as this offset, and the y-coordinate is adjusted by adding half of the `size.height`.
 */
internal fun Offset.toLeftCenter(size: Size): Offset {

    return Offset(x = x, y = y + size.height / 2)
}

/**
 * Calculates the offset of the right-center point of a rectangle,
 * given its top-left corner offset and its size.
 *
 * @param size The size of the rectangle.
 * @return The offset of the right-center point.
 */
internal fun Offset.toRightCenter(size: Size): Offset {

    return Offset(x = x + size.width, y = y + size.height / 2)
}

/**
 * Checks if this offset has neared another offset within a specified threshold.
 *
 * @param point The other offset to compare with.
 * @param threshold The maximum distance allowed for the offsets to be considered "neared".
 * Defaults to 24.0F.
 * @return `true` if the distance between this offset and the given point is less than or equal
 * to the threshold, `false` otherwise.
 */
internal fun Offset.hasNeared(point: Offset, threshold: Float = 24.0F): Boolean {

    return (this - point).getDistance() <= threshold
}
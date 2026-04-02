package io.bashpsk.zerodownload.domain.math

enum class ValueType {

    Positive,
    Negative,
    Unknown;

    companion object {

        fun find(value: Long): ValueType {

            return when (value) {

                in 0L..Long.MAX_VALUE -> Positive
                in Long.MIN_VALUE..0L -> Negative
                else -> Unknown
            }
        }

        fun find(value: Int): ValueType {

            return when (value) {

                in 0..Int.MAX_VALUE -> Positive
                in Int.MIN_VALUE..0 -> Negative
                else -> Unknown
            }
        }

        fun find(value: Float): ValueType {

            return when (value) {

                in 0.0F..Float.MAX_VALUE -> Positive
                in Float.MIN_VALUE..0.0F -> Negative
                else -> Unknown
            }
        }

        fun find(value: Double): ValueType {

            return when (value) {

                in 0.0..Double.MAX_VALUE -> Positive
                in Double.MIN_VALUE..0.0 -> Negative
                else -> Unknown
            }
        }
    }
}
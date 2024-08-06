/**
 * Checks whether the given data is valid and exists
 *
 * @param data test data
 */
export function hasData(data: any) {
    return data !== null && data !== undefined
}

/**
 * Checks if the given object is null, undefined or empty
 *
 * @param object data
 */
export function emptyObject(object: any) {

    for (const prop in object) {
        if (Object.hasOwn(object, prop)) {
            return false
        }
    }

    return true
}

/**
 * Formats a number for pretty display
 *
 * @param val number to format
 */
export function formatNumberForDisplay(val: number | string) {

    if (typeof val === 'string') {
        try {
            return Number(val).toLocaleString()
        } catch (e) {
            return 'DATA ERROR!'
        }
    }

    return val.toLocaleString()
}

/**
 * Displays negative points with bracket instead of negative sign
 *
 * @param val number
 */
export function formatNegativePoints(val: number) {

    if (val < 0) {
        return '(' + formatNumberForDisplay(Math.abs(val)) + ')'
    }

    return val
}

export default hasData;
const safeVal = (func, fallbackValue) => {
    try {
        let value = func();
        return (value === null || value === undefined) ? fallbackValue : value;
    } catch (e) {
        return fallbackValue;
    }
}

export default safeVal;
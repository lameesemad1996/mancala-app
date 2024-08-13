module.exports = {
    transform: {
        "^.+\\.[tj]sx?$": "babel-jest",
    },
    transformIgnorePatterns: [
        "/node_modules/(?!(axios|other-module)/)", // Add any other problematic modules here
    ],
    moduleNameMapper: {
        "\\.(css|scss)$": "identity-obj-proxy",
    },
    testEnvironment: "jsdom",
    setupFilesAfterEnv: [
        "<rootDir>/setupTests.js"
    ]

};

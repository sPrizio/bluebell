// eslint-disable-next-line @typescript-eslint/no-require-imports
const fs = require("fs");
// eslint-disable-next-line @typescript-eslint/no-require-imports
const path = require("path");

// Ensure correct usage
if (process.argv.length !== 3) {
  console.error(
    "Missing environment argument. Please include one of: dev, staging, prod",
  );
  process.exit(1);
}

const envName = process.argv[2];
if (!["dev", "staging", "prod"].includes(envName)) {
  console.error(
    `Invalid argument ${envName}. Allowed values: dev, staging, prod`,
  );
  process.exit(1);
}

const rootFolder = path.resolve(__dirname, "..");
const srcFile = path.join(rootFolder, "config", `.env.${envName}`);
const destFile = path.join(rootFolder, ".env");

if (!fs.existsSync(srcFile)) {
  console.error(`Error: Source file ${srcFile} does not exist.`);
  process.exit(1);
}

fs.copyFileSync(srcFile, destFile);
console.log(`.env file has been updated with ${srcFile}`);

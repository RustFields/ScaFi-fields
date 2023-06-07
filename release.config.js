const config = require('semantic-release-preconfigured-conventional-commits')
const publishCommands = `
sed -i 's/version := .*/version := "\${nextRelease.version}"/g' build.sbt || exit 1
git add -A || exit 2
git commit -m "chore: [skip ci] update version in build.sbt" || exit 3
git push --force origin || exit 4
git tag -a -f \${nextRelease.version} \${nextRelease.version} -F CHANGELOG.md  || exit 5
git push --force origin \${nextRelease.version} || exit 6
sbt assembly || exit 7
`
const releaseBranches = ["main"]
config.branches = releaseBranches
config.plugins.push(
    ["@semantic-release/exec", {
        "publishCmd": publishCommands,
    }],
    ["@semantic-release/github", {
        "assets": [
            { "path": "target/scala*/*.jar" },
        ]
    }],
    ["@semantic-release/git", {
        "assets": ["CHANGELOG.md", "package.json"],
        "message": "chore(release)!: [skip ci] ${nextRelease.version} released"
    }],
)
module.exports = config
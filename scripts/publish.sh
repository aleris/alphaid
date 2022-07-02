#!/bin/sh

OLD_VERSION=`sed -n 's/^version = "\([0-9]\.[0-9]\.[0-9]\)"/\1/p' < build.gradle.kts`

IFS=. read -r version minor patch <<EOF
$OLD_VERSION
EOF

case "$1" in
patch) VERSION="$version.$minor.$((patch+1))"; ;;
major) VERSION="$((version+1)).0.0"; ;;
*)     VERSION="$version.$((minor+1)).0"; ;;
esac

echo "Bumping version from $OLD_VERSION to $VERSION"
sed -i -e 's/version = "[0-9]\.[0-9]\.[0-9]"/version = "'"${VERSION}"'"/' build.gradle.kts

echo "Replacing new version $VERSION in README.md"
sed -i -e 's/com\.semanticmart\.alphaid:alphaid:[0-9]\.[0-9]\.[0-9]/com\.semanticmart\.alphaid:alphaid:'"${VERSION}"'/' README.md
sed -i -e 's/version>[0-9]\.[0-9]\.[0-9]/version>'"${VERSION}"'/' README.md

echo "Committing new version $VERSION in git"
if git status --porcelain; then
  git config --global user.name 'aleris'
  git config --global user.email 'adrian.tosca@gmail.com'
  git add -A
  git commit -m "Bump version to ${VERSION}"
  git push
fi

echo "Deploying $VERSION to sonatype"
gradle publishToSonatype closeAndReleaseSonatypeStagingRepository

echo "Version bump completed"

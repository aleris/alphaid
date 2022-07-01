#!/bin/sh
VERSION=`sed -n 's/^version=\(.*\)/\1/p' < version.properties`
echo "Replacing new version $VERSION in README.md"
sed -i -e 's/com\.semanticmart\.alphaid:alphaid:[0-9]\.[0-9]\.[0-9]/com\.semanticmart\.alphaid:alphaid:'"${VERSION}"'/' README.md
sed -i -e 's/version>[0-9]\.[0-9]\.[0-9]/version>'"${VERSION}"'/' README.md

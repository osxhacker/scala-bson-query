//////////////////////////////
// Release Information
//////////////////////////////

ThisBuild / scmInfo := Some (
	ScmInfo (
		url ("https://github.com/osxhacker/scala-bson-query"),
		"scm:git@github.com:osxhacker/scala-bson-query.git"
		)
	)

ThisBuild / description := "DSL for creating MongoDB and ReactiveMongo queries."
ThisBuild / licenses := List(
	"Apache 2" -> new URI ("http://www.apache.org/licenses/LICENSE-2.0.txt").toURL ()
	)

ThisBuild / homepage := Some (
	url ("https://github.com/osxhacker/scala-bson-query")
	)

ThisBuild / developers := List (
	Developer (
		id = "osxhacker",
		name = "osxhacker",
		email = "",
		url = url ("https://github.com/osxhacker")
		)
	)

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true

// New setting for Central Portal.
ThisBuild / publishTo := {
	val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
	if (isSnapshot.value)
		Some ("central-snapshots" at centralSnapshots)
	else
		localStaging.value
	}

usePgpKeyHex ("F641E17BCC038C5F03BC558782967B70951E663A")


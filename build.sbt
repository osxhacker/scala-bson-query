//////////////////////////////
/// Manifest Constants
//////////////////////////////

val scala212 = "2.12.20"
val scala213 = "2.13.16"
val scala3 = "3.3.5"
val supportedScalaVersions =
	scala213 ::
	scala212 ::
	/* scala3 :: */
	Nil


//////////////////////////////
/// Global Project Settings
//////////////////////////////

ThisBuild / organization := "com.github.osxhacker"
ThisBuild / scalaVersion := scala213
ThisBuild / crossScalaVersions := supportedScalaVersions
ThisBuild / autoAPIMappings := true
ThisBuild / version := "1.0.0"


//////////////////////////////
/// Command Aliases
//////////////////////////////

addCommandAlias("recompile", "; clean ; compile")


//////////////////////////////
/// Project Definitions
//////////////////////////////

lazy val root = (project in file ("."))
	.settings (
		crossScalaVersions := Nil,
		name := "scala-bson-query",
		ScalaUnidoc / siteSubdirName := "latest/api",
		addMappingsToSiteDir(
			ScalaUnidoc / packageDoc / mappings,
			ScalaUnidoc / siteSubdirName
			),

		git.remoteRepo := "git@github.com:osxhacker/scala-bson-query.git",
		git.useGitDescribe := true,
		makeSite / mappings ++= Seq (
			file ("LICENSE") -> "LICENSE"
			),

		paradoxProperties += ("version" -> version.value),
		paradoxTheme := Some (builtinParadoxTheme ("generic"))
		)
	.aggregate (core, mongo, reactive)
	.enablePlugins(
		ParadoxPlugin,
		SiteScaladocPlugin,
		ScalaUnidocPlugin,
		GhpagesPlugin
		)


lazy val core = module ("core")


lazy val mongo = module ("mongo")
	.settings (
		libraryDependencies ++= Seq (
			"org.mongodb.scala" %% "mongo-scala-bson" % "4.5.0"
			)
		)
	.dependsOn(core)

lazy val reactive = module ("reactive")
	.settings (
		libraryDependencies ++= Seq (
			"org.reactivemongo" %% "reactivemongo-bson-api" % "1.0.10-noshaded"
			)
		)
	.dependsOn(core)


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


def module (name : String) : Project = module (name, file (s"modules/$name"))


def module (name : String, location : File) : Project =
	Project(s"scala-bson-query-$name", location)
		.settings (
			crossScalaVersions := supportedScalaVersions,
			scalacOptions := Seq (
				"-encoding", "UTF-8",
				"-target:jvm-1.8",
				"-deprecation",
				"-explaintypes",
				"-feature",
				"-unchecked",
				"-Xlog-reflective-calls",
				"-Ywarn-unused:-patvars,_"
				),

			addCompilerPlugin(
				"org.typelevel" % "kind-projector" % "0.13.3" cross CrossVersion.full
				),

			libraryDependencies ++= {
				CrossVersion.partialVersion (scalaVersion.value) match {
					case Some((2, _)) =>
						List (
							"org.scala-lang" % "scala-reflect" % scalaVersion.value
							)

					case _ =>
						Nil
					}
				} ::: List (
					"com.chuusai" %% "shapeless" % "2.3.8",
					"org.typelevel" %% "cats-core" % "2.7.0",
					"org.typelevel" %% "mouse" % "1.0.10",
					"org.scalatest" %% "scalatest" % "3.2.11" % "test"
					),

			Compile / scalacOptions ++= {
				CrossVersion.partialVersion (scalaVersion.value) match {
					case Some((2, n)) if n <= 12 =>
						Nil

					case _ =>
						"-Ymacro-annotations" ::
						Nil
					}
				}
			)


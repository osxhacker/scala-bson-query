//////////////////////////////
/// Manifest Constants
//////////////////////////////

val scala212 = "2.12.15"
val scala213 = "2.13.8"
val scala3 = "3.1.0"
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
		publish / skip := true,

		git.remoteRepo := "git@github.com:osxhacker/scala-bson-query.git",
		makeSite / mappings ++= Seq (
			file ("LICENSE") -> "LICENSE"
			),

		paradoxProperties += ("version" -> version.value),
		paradoxTheme := Some (builtinParadoxTheme ("generic")),
		previewPath := "target/paradox/site/main/index.html",
		siteSourceDirectory := target.value / "paradox" / "site" / "main"
		)
	.aggregate (core, mongo, reactive)
	.enablePlugins (SiteScaladocPlugin, ParadoxSitePlugin, GhpagesPlugin)


lazy val core = module ("core")
	.settings (
		libraryDependencies ++= Seq (
			)
		)

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
				"org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full
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


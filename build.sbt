val CirceVersion      = "0.9.3"
val EnumeratumVersion = "1.5.13"
val Http4sVersion     = "0.18.11"
val LogbackVersion    = "1.2.3"
val ScalaCheckVersion = "1.14.0"
val ScalaTestVersion  = "3.0.5"

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.12.6",
    libraryDependencies ++= List(
      "org.scalacheck" %% "scalacheck" % ScalaCheckVersion
    ),
    scalacOptions ++= Seq(
      "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8",                // Specify character encoding used by source files.
      "-explaintypes",                     // Explain type errors in more detail.
      "-feature"
    )
  )

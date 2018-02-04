name := "fp-for-mortals"

version := "0.1"

resolvers += Resolver.sonatypeRepo("releases")

scalaVersion in ThisBuild := "2.12.4"
scalacOptions in ThisBuild ++= Seq(
  "-language:_",
  "-Ypartial-unification",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  "com.github.mpilquist" %% "simulacrum"     % "0.11.0",
  "com.chuusai"          %% "shapeless"      % "2.3.3" ,
  "com.fommil"           %% "deriving-macro" % "0.9.0" ,
  "org.scalaz"           %% "scalaz-core"    % "7.2.19"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.5")
addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
)

initialCommands in console := "import scalaz._, Scalaz._; import simulacrum._"
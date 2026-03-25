**Contains classes to make OpModes more readable, and easier to code with less "noise".**

Example:
```
while (opModeIsActive() && !actionsDone) {
   follower.update();
   actionsDone = performActions();
   pedroTelemetry.pathTelemetry(pedroMessage);
}

private void performActions() {
   switch (pathState) {
       case 0:
           this.pedroMessage = "Going from wall to launch zone";
           pedroMotion.goPath(teamPaths.pathBetween(startPose, launchPose));
           if (motion.isPathComplete()) {
               shootBalls();
               incrementPathState(motion.isPathComplete());
           }
           break;
       case 1:
           this.pedroMessage = "Going from launch zone to ball pickup";
           pedroMotion.goPath(teamPaths.pathBetween(launchPose, startBallPickupPose));
           incrementPathState(motion.isPathComplete());
           break;
    ...
   }
}

private void incrementPathState(boolean isPathComplete) {
   if (isPathComplete) pathState++;
}
```
## Add to a project
In build.dependencies.gradle:
- Add maven { url = 'https://jitpack.io' } to repositories
- Add implementation 'com.github.alwoodd:PedroLib:0.2.0' to dependencies
  - Use the latest version available.

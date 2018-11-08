package org.firstinspires.ftc.teamcode;

public class CraterAutoOp extends AutoOp {
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        //Descend Bot from Lander
        lowerBot();
        resetEncoders();

        driveTrain.lateralDistance(4);
        resetEncoders();

        driveTrain.longitudinalDistance(-4);
        resetEncoders();

        driveTrain.lateralDistance(-4);

        //While the Gold Position is undetermined, keep updating the camera
        cam.activateTFOD();
        while(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL)
            cam.update();
        cam.stopTFOD();

        //Move bot based on where the Gold Mineral is
        switch(cam.getGoldPosition()) {
            case CENTER:
                driveTrain.longitudinalDistance(-12);
                break;
            case LEFT:
                driveTrain.lateralDistance(-12);
                driveTrain.longitudinalDistance(-12);
                break;
            case RIGHT:
                driveTrain.lateralDistance(12);
                driveTrain.longitudinalDistance(-12);
                break;
        }

        //Crater Method()
        driveTrain.longitudinalDistance(-20);


    }

}

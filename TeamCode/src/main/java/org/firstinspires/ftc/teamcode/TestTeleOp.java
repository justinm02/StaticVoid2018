package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TestTeleOp", group = "TeleOp")

public class TestTeleOp extends OpMode {

    private PIDControl driveTrainPID;
    private double targetXPower, targetYPower;
    private DcMotor rearLeft, rearRight, frontLeft, frontRight;


    @Override
    public void init() {
        driveTrainPID = new PIDControl(0,0,0);
        targetXPower = 0;
        targetYPower = 0;
        rearLeft = hardwareMap.get(DcMotor.class, "backLeft");
        rearRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    }

    @Override
    public void loop() {
        targetXPower = this.gamepad1.left_stick_x;
        targetYPower = -this.gamepad1.left_stick_y;
        //fourDirectionalMovement();
        //omniDirectionalMovement();
        if(gamepad1.a) {
            targetYPower = .5;
        } else if(gamepad1.b) {
            targetYPower = -.5;
        } else {
            targetYPower = 0;
        }

        frontLeft.setPower(targetYPower);
        frontRight.setPower(-targetYPower);



        telemetry.addData("Target X Power: ", targetXPower);
        telemetry.addData("Target Y Power: ", targetYPower);
        telemetry.addData("Gamepad1 Left Stick X: ", gamepad1.left_stick_x);
        telemetry.addData("Gamepad1 Left Stick Y: ", gamepad1.left_stick_y);
    }

    /*
     * Moves the Robot in only the four cardinal directions, depending on which controller axis is being pushed the furthest.
     * Goes forward, backward, left, or right.
     */
    //Untested
    public void fourDirectionalMovement() {
        if(Math.abs(targetXPower) > Math.abs(targetYPower)) {
            rearRight.setPower(targetXPower);
            rearLeft.setPower(-targetXPower);
            frontRight.setPower(-targetXPower);
            frontLeft.setPower(targetXPower);

        } else {
            frontLeft.setPower(targetYPower);
            frontRight.setPower(targetYPower);
            rearLeft.setPower(targetYPower);
            rearRight.setPower(targetYPower);
        }
    }

    /*
     * Moves the Robot in all 360 degrees of direction.
     * Takes thr average of the left controller stick direction and applies it to the Robot.
     */
    //Untested
    public void omniDirectionalMovement() {
        rearRight.setPower(targetXPower + targetYPower);
        frontRight.setPower(-targetXPower + targetYPower);
        rearLeft.setPower(-targetXPower + targetYPower);
        frontLeft.setPower(targetXPower + targetYPower);
    }

}

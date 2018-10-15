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
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);

        targetXPower = gamepad1.left_stick_x;
        targetYPower = gamepad1.left_stick_y;
        fourDirectionalMovement();
        //omniDirectionalMovement();
        rotate();

        telemetry.addData("Gamepad1 Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Gamepad1 Left Stick Y", gamepad1.left_stick_y);
    }

    /*
     * Moves the Robot in only the four cardinal directions, depending on which controller axis is being pushed the furthest.
     * Goes forward, backward, left, or right.
     */
    //Untested
    public void fourDirectionalMovement() {
        boolean lateral;
        if(Math.abs(targetXPower) > Math.abs(targetYPower)) {
            rearRight.setPower(-targetXPower);
            rearLeft.setPower(-targetXPower);
            frontRight.setPower(targetXPower);
            frontLeft.setPower(targetXPower);
            lateral = true;
        } else {
            frontLeft.setPower(-targetYPower);
            frontRight.setPower(targetYPower);
            rearLeft.setPower(-targetYPower);
            rearRight.setPower(targetYPower);
            lateral = false;
        }
        telemetry.addData("Lateral", lateral);
        telemetry.addData("frontRight Power", frontRight.getPower());
        telemetry.addData("frontLeft Power", frontLeft.getPower());
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

    public void rotate() {
        float targetRotatePower = gamepad1.right_stick_x;
        rearRight.setPower(targetRotatePower);
        frontRight.setPower(targetRotatePower);
        rearLeft.setPower(targetRotatePower);
        frontLeft.setPower(targetRotatePower);
    }

}

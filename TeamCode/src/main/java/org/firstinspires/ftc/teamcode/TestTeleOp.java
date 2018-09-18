package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TestTeleOp", group = "TeleOp")

public class TestTeleOp extends OpMode {

    private PIDControl driveTrainPID;
    private double targetXPower, targetYPower;
    private DcMotor leftRear, rightRear, leftFront, rightFront;

    @Override
    public void init() {
        driveTrainPID = new PIDControl(0,0,0);
        targetXPower = 0;
        targetYPower = 0;
        //Comment
    }

    @Override
    public void loop() {
        targetXPower = this.gamepad1.left_stick_x;
        targetYPower = -this.gamepad1.left_stick_y;
        fourDirectionalMovement();
        //omniDirectionalMovement();
    }

    //Untested
    public void fourDirectionalMovement() {
        if(Math.abs(targetXPower) > Math.abs(targetYPower)) {
            rightRear.setPower(targetXPower);
            leftRear.setPower(-targetXPower);
            rightFront.setPower(-targetXPower);
            leftFront.setPower(targetXPower);
        } else {
            leftFront.setPower(targetYPower);
            rightFront.setPower(targetYPower);
            leftRear.setPower(targetYPower);
            rightRear.setPower(targetYPower);
        }
    }


    //Untested
    public void omniDirectionalMovement() {
        rightRear.setPower(targetXPower + targetYPower);
        rightFront.setPower(-targetXPower + targetYPower);
        leftRear.setPower(-targetXPower + targetYPower);
        leftFront.setPower(targetXPower + targetYPower);
    }

}

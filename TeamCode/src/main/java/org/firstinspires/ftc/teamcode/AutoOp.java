package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Autonomous", group = "Autonomous")
public abstract class AutoOp extends LinearOpMode {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift;

    private DriveTrain driveTrain;

    //Order of Operations:
    //
    //1) Lower Robot
    //2) Place Marker
    //3) Knock gold square
    //4) Lean on Crater

    public void initialize() {

        //Instantiating the Motors
        rearLeft = hardwareMap.get(DcMotorEx.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "rearRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");

        //Set the motors to travel based on a position
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reverse the Right Motors
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        //lift = hardwareMap.get(DcMotorEx.class, "lift");

        //Instantiates a Drive Train with the motors set to the correct mode for autonomous
        driveTrain = new DriveTrain(rearLeft, rearRight, frontLeft, frontRight);
    }

    //Loop required for all Autonomous modes
    @Override
    public abstract void runOpMode() throws InterruptedException;


    //Lowers the lift to detach from the lander
    public void lowerLift() {

    }

    //Sets the current position of the robot to 0
    public void resetEncoders() {

        //Set the encoder position back to 0
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //Tell the motors to go back to position based travel (Might be unnecessary based on type of motor used)
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }



}

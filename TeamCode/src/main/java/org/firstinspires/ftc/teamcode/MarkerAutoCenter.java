package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "MarkerCenter", group = "Autonomous")
public class MarkerAutoCenter extends AutoOp {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        initInit();
        while(!isStarted()) {
            intake.lift(-.075);
        }
        waitForStart();
        //playSurprise();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Hits center mineral
        driveTrain.longitudinalDistance(-18, 0.3);

    }
}

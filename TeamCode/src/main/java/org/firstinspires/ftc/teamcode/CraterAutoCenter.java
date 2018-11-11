package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CraterCenter", group = "Autonomous")
public class CraterAutoCenter extends AutoOp {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        while(!isStarted()) {
            intake.lift(-.075);
        }
        waitForStart();
        //playSurprise();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Hits center mineral, moves into friendly team's crater
        driveTrain.longitudinalDistance(-26, 0.3);

    }
}

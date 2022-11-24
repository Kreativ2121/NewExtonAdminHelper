//package pl.newexton;
//
//import com.jagrosh.jdautilities.command.CommandClientBuilder;
//import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
//import com.jagrosh.jdautilities.examples.command.AboutCommand;
//import com.jagrosh.jdautilities.examples.command.PingCommand;
//import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
//import net.dv8tion.jda.api.JDABuilder;
//import net.dv8tion.jda.api.OnlineStatus;
//import net.dv8tion.jda.api.Permission;
//import net.dv8tion.jda.api.entities.Activity;
//import pl.newexton.commands.ChooseCommand;
//import pl.newexton.commands.HelloCommand;
//
//import java.awt.*;
//import java.io.IOException;
//import java.time.Duration;
//
//import static pl.newexton.MCDataParser.downloadMCData;
//
//public class NewExtonBot {
//
//    static ServerData data = null;
//
//
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        //MTA0NDAwOTE1NDMxNjA4MzI1MQ.GQIqkM.C182I02ZDQisxXzM6BJ_u6WWnXkVmfcE_NvGgc
//
//        EventWaiter waiter = new EventWaiter();
//        CommandClientBuilder client = new CommandClientBuilder();
//        client.useDefaultGame();
//        client.setOwnerId("382925502672928768");
//        // sets emojis used throughout the bot on successes, warnings, and failures
//        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
//
//        // sets the bot prefix
//        client.setPrefix("!!");
//
////        client.addCommands(
//////                // command to show information about the bot
//////                new AboutCommand(Color.BLUE, "an example bot",
//////                        new String[]{"Cool commands","Nice examples","Lots of fun!"},
//////                        Permission.ADMINISTRATOR),
//////
//////                // command to make a random choice
//////                new ChooseCommand(),
//////
//////                // command to say hello
//////                new HelloCommand(waiter),
//////
//////                // command to check bot latency
//////                new PingCommand(),
////
////                // command to shut off the bot
////                new ShutdownCommand());
//
//        JDABuilder.createDefault("MTA0NDAwOTE1NDMxNjA4MzI1MQ.GQIqkM.C182I02ZDQisxXzM6BJ_u6WWnXkVmfcE_NvGgc")
//                .setStatus(OnlineStatus.ONLINE)
//                .setActivity(Activity.playing("Tekken 8"))
//                .addEventListeners(waiter, client.build())
//                .build();
//
//        downloadMCData();
//
//
//
//    }
//}
package pl.newexton;

/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;
import static pl.newexton.MCDataParser.downloadMCData;

public class NewExtonBot extends ListenerAdapter
{
    static ServerData mcServerData = null;

    public static void main(String[] args) throws IOException {
        System.out.println("NewExtonBot uruchomiony poprawnie.");
        mcServerData = downloadMCData();

        JDA jda = JDABuilder.createLight("xxxxx", EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
                .addEventListeners(new NewExtonBot())
                .build();

        // These commands might take a few minutes to be active after creation/update/delete
        CommandListUpdateAction commands = jda.updateCommands();

        // Moderation commands with required options
//        commands.addCommands(
//                Commands.slash("ban", "Ban a user from this server. Requires permission to ban users.")
//                        .addOptions(new OptionData(USER, "user", "The user to ban") // USER type allows to include members of the server or other users by id
//                                .setRequired(true)) // This command requires a parameter
//                        .addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days.") // This is optional
//                                .setRequiredRange(0, 7)) // Only allow values between 0 and 7 (inclusive)
//                        .addOptions(new OptionData(STRING, "reason", "The ban reason to use (default: Banned by <user>)")) // optional reason
//                        .setGuildOnly(true) // This way the command can only be executed from a guild, and not the DMs
//                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)) // Only members with the BAN_MEMBERS permission are going to see this command
//        );

        // Simple reply commands
//        commands.addCommands(
//                Commands.slash("say", "Makes the bot say what you tell it to")
//                        .addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
//        );

        // Simple reply commands
        commands.addCommands(
                Commands.slash("minecraft", "Checks the information about Minecraft Server.")
                        //.addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
        );

//        commands.addCommands(
//                Commands.slash("prune", "Prune messages from this channel")
//                        .addOption(INTEGER, "amount", "How many messages to prune (Default 100)") // simple optional argument
//                        .setGuildOnly(true)
//                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE))
//        );

        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        switch (event.getName())
        {
//            case "ban":
//                Member member = event.getOption("user").getAsMember(); // the "user" option is required, so it doesn't need a null-check here
//                User user = event.getOption("user").getAsUser();
//                ban(event, user, member);
//                break;
//            case "say":
//                say(event, event.getOption("content").getAsString()); // content is required so no null-check here
//                break;
//            case "prune": // 2 stage command with a button prompt
//                prune(event);
//                break;
            case "minecraft":
                try {
                    System.out.println("Odpowiadam na żądanie /minecraft.\nPobieranie danych z API serwera...");
                    mcServerData = downloadMCData();
//                    createTextChannel(event.getMember(),"\uD835\uDE1A\uD835\uDE26\uD835\uDE33\uD835\uDE38\uD835\uDE26\uD835\uDE33 \uD835\uDE14\uD835\uDE0A:" + mcServerData.online);
//                    createTextChannel(event.getMember(),"Liczba graczy: " + mcServerData.players.now + "/" + mcServerData.players.max);

                    //FAIL
                    if(Objects.equals(mcServerData.status, "failure")){
                        System.err.println("Nie udało się pobrać statusu serwera.");
                        String reply = "Nie udało się pobrać statusu serwera.";
                        event.reply(reply).queue();
                        break;
                    }

                    //CORRECT REPLY
                    StringBuilder reply = new StringBuilder();
                    if(mcServerData.online){
                        reply.append("Serwer MC: Online\n");

                    } else {
                        reply.append("Serwer MC: Offline\n");
                    }
                    reply.append("Liczba graczy: ").append(mcServerData.players.now).append("/").append(mcServerData.players.max);

                    if(mcServerData.players.now > 0){
                        reply.append("\nAktywni gracze: ");
                    }

                    int noOfPlayers = mcServerData.players.sample.size();
                    for(int i = 0; i < noOfPlayers; i++){
                        LinkedTreeMap player = (LinkedTreeMap) mcServerData.players.sample.get(i);
                        if(i<noOfPlayers-1){
                            reply.append(player.get("name")).append(", ");
                        }
                        else{
                            reply.append(player.get("name"));
                        }
                    }
                    event.reply(reply.toString()).queue();
                    System.out.println(reply.toString());
                    System.out.println("Odpowiedziano na żądanie.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                break;
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        String[] id = event.getComponentId().split(":"); // this is the custom id we specified in our button
        String authorId = id[0];
        String type = id[1];
        // Check that the button is for the user that clicked it, otherwise just ignore the event (let interaction fail)
        if (!authorId.equals(event.getUser().getId()))
            return;
        event.deferEdit().queue(); // acknowledge the button was clicked, otherwise the interaction will fail

        MessageChannel channel = event.getChannel();
        switch (type)
        {
            case "prune":
                int amount = Integer.parseInt(id[2]);
                event.getChannel().getIterableHistory()
                        .skipTo(event.getMessageIdLong())
                        .takeAsync(amount)
                        .thenAccept(channel::purgeMessages);
                // fallthrough delete the prompt message with our buttons
            case "delete":
                event.getHook().deleteOriginal().queue();
        }
    }

    public void ban(SlashCommandInteractionEvent event, User user, Member member)
    {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else
        InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
        hook.setEphemeral(true); // All messages here will now be ephemeral implicitly
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS))
        {
            hook.sendMessage("You do not have the required permissions to ban users from this server.").queue();
            return;
        }

        Member selfMember = event.getGuild().getSelfMember();
        if (!selfMember.hasPermission(Permission.BAN_MEMBERS))
        {
            hook.sendMessage("I don't have the required permissions to ban users from this server.").queue();
            return;
        }

        if (member != null && !selfMember.canInteract(member))
        {
            hook.sendMessage("This user is too powerful for me to ban.").queue();
            return;
        }

        // optional command argument, fall back to 0 if not provided
        int delDays = event.getOption("del_days", 0, OptionMapping::getAsInt); // this last part is a method reference used to "resolve" the option value

        // optional ban reason with a lazy evaluated fallback (supplier)
        String reason = event.getOption("reason",
                () -> "Banned by " + event.getUser().getAsTag(), // used if getOption("reason") is null (not provided)
                OptionMapping::getAsString); // used if getOption("reason") is not null (provided)

        // Ban the user and send a success response
        event.getGuild().ban(user, delDays, TimeUnit.DAYS)
                .reason(reason) // audit-log ban reason (sets X-AuditLog-Reason header)
                .flatMap(v -> hook.sendMessage("Banned user " + user.getAsTag())) // chain a followup message after the ban is executed
                .queue(); // execute the entire call chain
    }

    public void say(SlashCommandInteractionEvent event, String content)
    {
        event.reply(content).queue(); // This requires no permissions!
    }

//    public void leave(SlashCommandInteractionEvent event)
//    {
//        if (!event.getMember().hasPermission(Permission.KICK_MEMBERS))
//            event.reply("You do not have permissions to kick me.").setEphemeral(true).queue();
//        else
//            event.reply("Leaving the server... :wave:") // Yep we received it
//                    .flatMap(v -> event.getGuild().leave()) // Leave server after acknowledging the command
//                    .queue();
//    }

    public static void createTextChannel(Member member, String name) {
        Guild guild = member.getGuild();

        guild.createTextChannel(name)
                .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .queue(); // this actually sends the request to discord.
    }

    public void prune(SlashCommandInteractionEvent event)
    {
        OptionMapping amountOption = event.getOption("amount"); // This is configured to be optional so check for null
        int amount = amountOption == null
                ? 100 // default 100
                : (int) Math.min(200, Math.max(2, amountOption.getAsLong())); // enforcement: must be between 2-200
        String userId = event.getUser().getId();
        event.reply("This will delete " + amount + " messages.\nAre you sure?") // prompt the user with a button menu
                .addActionRow(// this means "<style>(<id>, <label>)", you can encode anything you want in the id (up to 100 characters)
                        Button.secondary(userId + ":delete", "Nevermind!"),
                        Button.danger(userId + ":prune:" + amount, "Yes!")) // the first parameter is the component id we use in onButtonInteraction above
                .queue();
    }
}

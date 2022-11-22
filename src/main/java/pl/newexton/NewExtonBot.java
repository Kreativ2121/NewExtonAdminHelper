package pl.newexton;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

import static pl.newexton.MCDataParser.downloadMCData;

public class NewExtonBot {

    static ServerData data = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        DiscordClient client = DiscordClient.create("MTA0NDAwOTE1NDMxNjA4MzI1MQ.GQIqkM.C182I02ZDQisxXzM6BJ_u6WWnXkVmfcE_NvGgc");

        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) ->
                gateway.on(ReadyEvent.class, event ->
                        Mono.fromRunnable(() -> {
                            final User self = event.getSelf();
                            System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                        })));

        //downloadMCData();

        while(true){
            data = downloadMCData();
            Thread.sleep(5000);
        }


    }
}
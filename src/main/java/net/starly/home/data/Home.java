package net.starly.home.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Home {

    private final UUID owner;

    private final Location location;

}

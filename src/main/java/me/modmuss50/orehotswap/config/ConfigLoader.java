package me.modmuss50.orehotswap.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by mark on 28/03/16.
 */
public class ConfigLoader {

	File dir;

	public HashMap<String, OreConfig> configFiles = new HashMap<>();

	public ConfigLoader(File dir) {
		this.dir = dir;
		if (!dir.exists()) {
			dir.mkdirs();
		}
		createExampleJsonFile();
	}

	public void load() throws FileNotFoundException {
		if (!configFiles.isEmpty()) {
			configFiles.clear();
		}
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".json")) {
				loadFile(file);
			}
		}
	}

	private void loadFile(File file) throws FileNotFoundException {
		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Type typeOfHashMap = new TypeToken<OreConfig>() {
		}.getType();
		configFiles.put(file.getName().replace(".json", ""), (OreConfig) gson.fromJson(reader, typeOfHashMap));
	}

	private void createExampleJsonFile() {
		File output = new File(dir, "example.json");
		if (!output.exists()) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			OreConfig config = new OreConfig("minecraft:diamond_block", "minecraft:stone", 0, 7, 3, 5, 64, 0);
			String json = gson.toJson(config);
			try {
				FileWriter writer = new FileWriter(output);
				writer.write(json);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

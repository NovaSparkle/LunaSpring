package org.novasparkle.lunaspring.API.util.utilities.localization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.LunaSpring;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ILang {
    String URL_PATH = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/[version]/assets/minecraft/lang/[lang].json";
    String DEFAULT_LANG_VERSION = "26.1.2";

    String getLang();
    Map<String, String> getTranslations();
    String translate(String path, Supplier<String> orElse);
    String getPath(String key);
    <M extends Map<String, String>> M getReversedTranslations();
    default CompletableFuture<File> download(String version) {
        return CompletableFuture.supplyAsync(() -> {
            String strUrl = URL_PATH.replace("[lang]", getLang()).replace("[version]", version);
            try {
                File pluginFolder = new File(LunaSpring.getInstance().getDataFolder(), "localizeLangs/");
                if (!pluginFolder.exists()) {
                    pluginFolder.mkdirs();
                }

                File outputFile = new File(pluginFolder, getLang() + ".json");
                URL url = URI.create(strUrl).toURL();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("User-Agent", "MinecraftPlugin/1.0");

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP " + responseCode + " при скачивании языка");
                }

                try (InputStream in = connection.getInputStream()) {
                    Files.copy(in, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                return outputFile;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    default void load(File file) {
        try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> loaded = gson.fromJson(reader, type);

            getTranslations().clear();
            getTranslations().putAll(loaded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    default <E> E detranslate(String translatedValue, Function<String[], E> func) {
        String path = getPath(translatedValue);
        if (path == null) return null;

        String[] parts = path.split("\\.");
        return func.apply(parts);
    }
}

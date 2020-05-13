package lunarlander;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.common.io.Files;
import com.google.gson.JsonObject;

import java.io.*;
// import java.util.HashMap;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;


/**
 * Class operating on attributes and methods linked to
 * the configuration.json file that stores initial game state.
 */
public class Configuration {
  // Serialize and deserialize only Configuration fields, that is
  // moonMaps and params

  /**
   * Constructor making ArrayList of Moon class objects {@link Moon} -
   * each Moon has its own surface shape. We will store
   * them in moonMaps.
   */
  public Configuration() {
    this.moonMaps = new ArrayList<Moon>();
  }

  /**
   * Method serialising moonMaps into Json and saving it
   * in configuration.json file.
   */
  public void toFile() {
    // serialization
    Moon[] moonMapsArray = this.moonMaps.toArray(new Moon[this.moonMaps.size()]);
    String json = this.gson.toJson(moonMapsArray);
    // saving to file
    File file = new File("src/main/resources/lunarlander/configuration.json");
    try {
      CharSink sink = Files.asCharSink(file, Charsets.UTF_8);
      sink.write(json);
    } catch (IOException e) {
      System.out.println("Something went wrong. Possible reasons: ");
      System.out.println("1) Folder you are trying to place file in does not exist.");
      System.out.println("2) You are running out of disk space.");
      System.out.println("3) You don't have permissions to place a file here.");
    }
  }

  /**
   * Method reading configuration.json file and deserializing its
   * content back into moonMaps ArrayList.
   *
   * @param filePath Specifying where does configuration.json exist, String.
   */
  public void fromFile(String filePath) {
    File file = new File(filePath);
    CharSource source = Files.asCharSource(file, Charsets.UTF_8);
    try {
      String result = source.read();
      Moon[] moonMapsArray = this.gson.fromJson(result, Moon[].class);
      this.moonMaps = Lists.newArrayList(moonMapsArray);
    } catch (IOException e) {
      System.out.println("Something went wrong. Possible reasons: ");
      System.out.println("1) Folder your are trying to open does not exist.");
      System.out.println("2) You don't have permissions to open that file.");
    }
  }

  /**
   * Method returning moonMap of chosen level.
   *
   * @param lvl Number of level we want to get surface shape of, int.
   * @return Moon of moonMaps at lvl-1 index, {@link Moon}.
   */
  public Moon getMoonMap(int lvl) {
    return moonMaps.get(lvl-1);
  }

  /**
   * Method adding or overwriting moonMap of chosen level.
   *
   * @param lvl Number of level we want to create moon surface with, int.
   */

  public void lunarClient() {
    try {
      // Opening socket chosen by client
      Socket socket = new Socket("localhost", 21370);

      // Data stream we are directing to server
      OutputStream os = socket.getOutputStream();

      // Stream writer
      PrintWriter pw = new PrintWriter(os, true);
      System.out.println("Sending request");
      pw.println("GET");

      // Response stream
      InputStream is = socket.getInputStream();
      // Reader from stream
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      // StringBuffer if we are going to put it on a different thread
      StringBuffer sb = new StringBuffer();
      // Helper variable holding current line
      String _line = "";
      // Read all the response lines to the end
      while ((_line = br.readLine()) != null) {
        // Append every line to StringBuffer
        sb.append(_line);
        sb.append('\n');
      }
      saveFile(sb);

      System.out.print("Imported map configuration file from the server successfully.");
      socket.close();
      br.close();
    } catch (Exception e) {
      System.err.println("Client exception: " + e);
    }
  }

  /**
   * Save configurations of maps received from the server in
   * a configuration_fromserver.json file. After that method
   * sets configDownloaded flag on true.
   * @param serverMapsResponse Response we are getting from
   *                           the server when we send GET request
   */
  public void saveFile(StringBuffer serverMapsResponse) {
    String json = serverMapsResponse.toString();
    // saving to file
    File file = new File("src/main/resources/lunarlander/configuration_fromserver.json");
    try {
      CharSink sink = Files.asCharSink(file, Charsets.UTF_8);
      sink.write(json);
      setConfigDownloaded(true);
    } catch (IOException e) {
      System.out.println("Something went wrong. Possible reasons: ");
      System.out.println("1) Folder you are trying to place file in does not exist.");
      System.out.println("2) You are running out of disk space.");
      System.out.println("3) You don't have permissions to place a file here.");
    }
  }

  public void generateLevel(int lvl) {
    this.moonMaps.add(new Moon(lvl));
  }

  /**
   * @return true if maps are the latest ones from
   * the server otherwise false
   */
  public boolean isConfigDownloaded() { return configDownloaded; }

  /**
   * Set 'configDownloaded' status
   * @param configDownloaded true if maps are the
   * latest ones from the server otherwise false
   */
  public void setConfigDownloaded(boolean configDownloaded) {
    this.configDownloaded = configDownloaded;
  }


  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private ArrayList<Moon> moonMaps;

  /**
   * Flag controlling whether current maps are
   * the same as the ones from the latest
   * config file from the server
   */
  boolean configDownloaded = false;

  // For future use:
  // private HashMap<String, String> params; // for example for server ip and port
}

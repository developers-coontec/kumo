package com.kennycason.kumo;

import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import java.awt.Color;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomWordCloud {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomWordCloud.class);

  private WordCloud wordCloud;

  public CustomWordCloud(int width, int height, int numberRandomColor) {
    loadFonts();
    final Dimension dimension = new Dimension(width, height);
    wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(1);
    wordCloud.setBackgroundColor(Color.WHITE);
    wordCloud.setBackground(new RectangleBackground(dimension));
    // wordCloud.setAngleGenerator(new AngleGenerator(-60, 60, 5));
    wordCloud.setColorPalette(buildRandomColorPalette(numberRandomColor));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new LinearFontScalar(18, 70));
  }

  public CustomWordCloud() {

  }

  public void loadContents(List<String> contents) {
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(contents);
    wordCloud.build(wordFrequencies);
  }

  public void loadContentsWithFrequencies(List<WordFrequency> wordFrequencies) {
    wordCloud.build(wordFrequencies);
  }

  public String writeToBase64String() {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    wordCloud.writeToStream("png", bos);
    return Base64.getEncoder().encodeToString(bos.toByteArray());
  }

  protected KumoFont AppleSDGothicNeoB = null;
  protected KumoFont AppleSDGothicNeoEB = null;
  protected KumoFont AppleSDGothicNeoSB = null;
  private final Random RANDOM = new Random();

  public static List<String> readLinesFromInputStream(InputStream inputStream) throws IOException {
    return IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
  }

  public void writeToFile(String outFileName) {
    wordCloud.writeToFile(outFileName);
  }

  private void loadFonts() {
    AppleSDGothicNeoB = new KumoFont(getInputStream("font/AppleSDGothicNeoB.ttf"));
    AppleSDGothicNeoEB = new KumoFont(getInputStream("font/AppleSDGothicNeoEB.ttf"));
    AppleSDGothicNeoSB = new KumoFont(getInputStream("font/AppleSDGothicNeoSB.ttf"));
  }

  private Set<String> loadStopWords() {
    try {
      final List<String> lines = IOUtils.readLines(getInputStream("text/stop_words.txt"));
      return new HashSet<>(lines);

    } catch (final IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return Collections.emptySet();
  }

  private InputStream getInputStream(String path) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
  }


  private ColorPalette buildRandomColorPalette(final int n) {
    final Color[] colors = new Color[n];
    for (int i = 0; i < colors.length; i++) {
      colors[i] = new Color(RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25,
          RANDOM.nextInt(230) + 25);
    }
    return new ColorPalette(colors);
  }
}

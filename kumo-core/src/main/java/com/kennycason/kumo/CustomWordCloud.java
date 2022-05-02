package com.kennycason.kumo;

import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer;
import com.kennycason.kumo.nlp.tokenizer.core.KomoranWordTokenizer;
import com.kennycason.kumo.nlp.tokenizer.core.TwitterWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.wordstart.CenterWordStart;
import java.awt.Color;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
    wordCloud.setAngleGenerator(new AngleGenerator(0, 0, 3));
    wordCloud.setWordStartStrategy(new CenterWordStart());
    wordCloud.setColorPalette(CustomWordCloud.buildRandomColorPalette(numberRandomColor));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new LinearFontScalar(18, 70));
  }

  public CustomWordCloud() {
  }

  public void loadContentsWithFrequencies(List<WordFrequency> wordFrequencies) {
    wordCloud.build(wordFrequencies);
  }

  public void loadContents(WordTokenizer tokenizer, List<String> contents, int minimumFrequency,
      Integer topLimit) {
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordTokenizer(tokenizer);
    final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(contents);
    List<WordFrequency> resultFrequencies = new ArrayList<>();
    for (WordFrequency wordFrequency : wordFrequencies) {
      if (minimumFrequency <= wordFrequency.getFrequency()) {
        resultFrequencies.add(wordFrequency);
      }
    }
    if (topLimit != null) {
      resultFrequencies = frequencyAnalyzer.takeTopFrequencies(
          resultFrequencies, topLimit);
    }
    System.out.println(resultFrequencies.toString());
    loadContentsWithFrequencies(resultFrequencies);
  }

  public void loadContentsWithKomoranTokenizer(List<String> contents) {
    this.loadContentsWithKomoranTokenizer(contents, 1, null);
  }

  public void loadContentsWithKomoranTokenizer(List<String> contents, int minimumFrequency,
      Integer topLimit) {
    loadContents(new KomoranWordTokenizer(), contents, minimumFrequency, topLimit);
  }

  public void loadContentsWithTwitterTokenizer(List<String> contents) {
    this.loadContentsWithTwitterTokenizer(contents, 1, null);
  }

  public void loadContentsWithTwitterTokenizer(List<String> contents, int minimumFrequency,
      Integer topLimit) {
    loadContents(new TwitterWordTokenizer(), contents, minimumFrequency, topLimit);
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


  private static final List<Color> WORD_CLOUD_COLORS = Arrays.asList(
      new Color(30, 175, 216), // blue
      new Color(247, 214, 0), //sunflower-yellow
      new Color(244, 174, 26), // squash
      new Color(243, 107, 40), // dusty-orange
      new Color(239, 73, 36), // tomato
      new Color(238, 47, 68), // strawberry
      new Color(184, 86, 161), // ugly-purple
      new Color(145, 91, 166), // dark-lilac
      new Color(41, 56, 98), // dark-grey-blue
      new Color(81, 139, 201), // cool-blue
      new Color(39, 190, 182), // tealish
      new Color(103, 191, 107), // soft-green
      new Color(161, 205, 73) // booger
  );

  public static ColorPalette buildRandomColorPalette(final int n) {
    final Color[] colors = new Color[n];
    List<Color> newList = new ArrayList<>(WORD_CLOUD_COLORS);
    Collections.shuffle(newList);
    for (int i = 0; i < colors.length; i++) {
      colors[i] = newList.get((i % WORD_CLOUD_COLORS.size()));
    }
    return new ColorPalette(colors);
  }
}

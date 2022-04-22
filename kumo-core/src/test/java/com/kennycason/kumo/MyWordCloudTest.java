package com.kennycason.kumo;

import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundaryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.examples.WordCloudITest;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.wordstart.CenterWordStart;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWordCloudTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(WordCloudITest.class);

  private static final Random RANDOM = new Random();
  public static final List<WordFrequency> WORD_FREQUENCIES = Arrays
      .asList(new WordFrequency("apple", 22),
          new WordFrequency("baby", 3),
          new WordFrequency("back", 15),
          new WordFrequency("back1", 1),
          new WordFrequency("back2", 2),
          new WordFrequency("back3", 3),
          new WordFrequency("back4", 4),
          new WordFrequency("김치", 5),
          new WordFrequency("홍어", 6),
          new WordFrequency("홍길동", 7),
          new WordFrequency("back8", 8),
          new WordFrequency("야자수", 9),
          new WordFrequency("back10", 10),
          new WordFrequency("파인애플", 11),
          new WordFrequency("back12", 12),
          new WordFrequency("back13", 13),
          new WordFrequency("감자", 14),
          new WordFrequency("back15", 15),
          new WordFrequency("back16", 16),
          new WordFrequency("bear", 9),
          new WordFrequency("boy", 26));


  protected static KumoFont AppleSDGothicNeoB = null;
  protected static KumoFont AppleSDGothicNeoEB = null;
  protected static KumoFont AppleSDGothicNeoSB = null;

  private static void loadFonts() {
    AppleSDGothicNeoB = new KumoFont(getInputStream("font/AppleSDGothicNeoB.ttf"));
    AppleSDGothicNeoEB = new KumoFont(getInputStream("font/AppleSDGothicNeoEB.ttf"));
    AppleSDGothicNeoSB = new KumoFont(getInputStream("font/AppleSDGothicNeoSB.ttf"));
  }

  private static Set<String> loadStopWords() {
    try {
      final List<String> lines = IOUtils.readLines(getInputStream("text/stop_words.txt"));
      return new HashSet<>(lines);

    } catch (final IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return Collections.emptySet();
  }

  private static InputStream getInputStream(String path) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
  }


  @Test
  public void matchOnlineExample() throws IOException {
    loadFonts();
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordFrequenciesToReturn(600);
    frequencyAnalyzer.setMinWordLength(5);
    frequencyAnalyzer.setStopWords(loadStopWords());
//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new FileInputStream("/tmp/code.txt"));
    final Dimension dimension = new Dimension(600, 600);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(1);
    wordCloud.setBackground(new CircleBackground(300));
    wordCloud.setBackgroundColor(Color.WHITE);
    wordCloud.setColorPalette(CustomWordCloud.buildRandomColorPalette(2));
//    wordCloud.setKumoFont(new KumoFont("Helvitica", FontWeight.PLAIN));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new LinearFontScalar(8, 130));
    wordCloud.build(WORD_FREQUENCIES);
    wordCloud.writeToFile("output/custom_wordcloud_match_online_example.png");
  }

  @Test
  public void readCNN() throws IOException {
    loadFonts();
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordFrequenciesToReturn(500);
    frequencyAnalyzer.setMinWordLength(3);
    frequencyAnalyzer.setStopWords(loadStopWords());

    final List<WordFrequency> wordFrequencies = frequencyAnalyzer
        .load(new URL("http://www.cnn.com/"));
    final Dimension dimension = new Dimension(600, 600);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(1);
    wordCloud.setBackground(new RectangleBackground(dimension));
//    wordCloud.setKumoFont(new KumoFont("Impact", FontWeight.PLAIN));
    // wordCloud.setAngleGenerator(new AngleGenerator(-60, 60, 5));
    wordCloud.setColorPalette(CustomWordCloud.buildRandomColorPalette(5));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new LinearFontScalar(18, 70));
//        wordCloud.build(wordFrequencies);
    wordCloud.build(WORD_FREQUENCIES);
    wordCloud.writeToFile("output/custom_cnn.png");
  }


  @Test
  public void datarankCircle() throws IOException {
    loadFonts();
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordFrequenciesToReturn(750);
    frequencyAnalyzer.setMinWordLength(5);
    frequencyAnalyzer.setStopWords(loadStopWords());

    final List<WordFrequency> wordFrequencies = frequencyAnalyzer
        .load(getInputStream("text/datarank.txt"));
    final Dimension dimension = new Dimension(600, 600);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(2);
    wordCloud.setBackground(new CircleBackground(300));
    wordCloud.setColorPalette(
        new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1),
            new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
    final long startTime = System.currentTimeMillis();
    wordCloud.build(WORD_FREQUENCIES);
//        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
    wordCloud.writeToFile("output/custom_datarank_wordcloud_circle_sqrt_font.png");
  }

  @Test
  public void whaleImgSmallTest() throws IOException {
    loadFonts();
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordFrequenciesToReturn(300);
    frequencyAnalyzer.setMinWordLength(5);
    frequencyAnalyzer.setStopWords(loadStopWords());

    final List<WordFrequency> wordFrequencies = frequencyAnalyzer
        .load(getInputStream("text/datarank.txt"));
    final Dimension dimension = new Dimension(500, 312);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(1);
    wordCloud
        .setBackground(new PixelBoundaryBackground(getInputStream("backgrounds/whale_small.png")));
    wordCloud.setColorPalette(
        new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1),
            new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new LinearFontScalar(10, 40));
    wordCloud.build(WORD_FREQUENCIES);
    wordCloud.writeToFile("output/custom_whale_wordcloud_small.png");
  }


  @Test
  public void simpleCircleTest() throws IOException {
    loadFonts();
//        final List<WordFrequency> wordFrequencies = buildWordFrequencies().subList(0, 150);
    final Dimension dimension = new Dimension(600, 600);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
    wordCloud.setPadding(0);
    wordCloud.setBackground(new CircleBackground(300));
    wordCloud.setColorPalette(CustomWordCloud.buildRandomColorPalette(20));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new LinearFontScalar(10, 40));
    wordCloud.build(WORD_FREQUENCIES);
    wordCloud.writeToFile("output/custom_wordcloud_circle.png");
  }

  @Test
  public void loadWikipediaFromUrl() throws IOException {
    loadFonts();
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordFrequenciesToReturn(1000);
    frequencyAnalyzer.setMinWordLength(1);

    final List<WordFrequency> wordFrequencies = frequencyAnalyzer
        .load(new URL("http://en.wikipedia.org/wiki/Main_Page"));
//        final Dimension dimension = new Dimension(1000, 1000);
    final Dimension dimension = new Dimension(400, 400);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(1);
    wordCloud.setBackground(new RectangleBackground(dimension));
    // wordCloud.setAngleGenerator(new AngleGenerator(-60, 60, 5));
    wordCloud.setAngleGenerator(new AngleGenerator(0, 0, 3));
    wordCloud.setWordStartStrategy(new CenterWordStart());
    wordCloud.setBackgroundColor(Color.WHITE);
    wordCloud.setColorPalette(CustomWordCloud.buildRandomColorPalette(1));
    wordCloud.setKumoFont(AppleSDGothicNeoB);
    wordCloud.setFontScalar(new LinearFontScalar(18, 70));
//        wordCloud.build(wordFrequencies);
    wordCloud.build(WORD_FREQUENCIES);
    wordCloud.writeToFile("output/custom_wikipedia.png");
  }

  @Test
  public void loadFromText() throws IOException{
//    loadFonts();
//    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
//    final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/korean_text.txt"));
//    final Dimension dimension = new Dimension(400, 400);
//    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
//    wordCloud.setPadding(1);
//    wordCloud.setBackground(new RectangleBackground(dimension));
//    // wordCloud.setAngleGenerator(new AngleGenerator(-60, 60, 5));
//    wordCloud.setColorPalette(buildRandomColorPalette(5));
//    wordCloud.setKumoFont(AppleSDGothicNeoB);
//    wordCloud.setFontScalar(new LinearFontScalar(18, 70));
//        wordCloud.build(wordFrequencies);
//    wordCloud.writeToFile("output/custom_load_text.png");
    CustomWordCloud customWordCloud = new CustomWordCloud(300, 300, 1);
//    customWordCloud.loadContents(CustomWordCloud.readLinesFromInputStream(getInputStream("text/korean_text.txt")));
    customWordCloud.loadContentsWithFrequencies(WORD_FREQUENCIES);
    customWordCloud.writeToFile("output/custom_load_text.png");
  }

  @Test
  public void loadFromText2() throws IOException{
//    loadFonts();
//    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
//    final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/korean_text.txt"));
//    final Dimension dimension = new Dimension(400, 400);
//    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
//    wordCloud.setPadding(1);
//    wordCloud.setBackground(new RectangleBackground(dimension));
//    // wordCloud.setAngleGenerator(new AngleGenerator(-60, 60, 5));
//    wordCloud.setColorPalette(buildRandomColorPalette(5));
//    wordCloud.setKumoFont(AppleSDGothicNeoB);
//    wordCloud.setFontScalar(new LinearFontScalar(18, 70));
//        wordCloud.build(wordFrequencies);
//    wordCloud.writeToFile("output/custom_load_text.png");
    CustomWordCloud customWordCloud = new CustomWordCloud(300, 300, 1);
    customWordCloud.loadContents(CustomWordCloud.readLinesFromInputStream(getInputStream("text/korean_text.txt")));
//    customWordCloud.loadContentsWithFrequencies(WORD_FREQUENCIES);
    customWordCloud.writeToFile("output/custom_load_text.png");
  }

}

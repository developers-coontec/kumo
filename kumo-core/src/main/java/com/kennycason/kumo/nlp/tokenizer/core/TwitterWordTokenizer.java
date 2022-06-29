package com.kennycason.kumo.nlp.tokenizer.core;

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer;
import com.twitter.penguin.korean.KoreanPosJava;
import com.twitter.penguin.korean.KoreanTokenJava;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer.KoreanToken;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Seq;

public class TwitterWordTokenizer implements WordTokenizer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TwitterWordTokenizer.class);

  @Override
  public List<String> tokenize(String sentence) {

    CharSequence normalized = TwitterKoreanProcessorJava.normalize(sentence);
    Seq<KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(normalized);
    Seq<KoreanToken> stemmed = TwitterKoreanProcessorJava.stem(tokens);
    List<KoreanTokenJava> javaTokens = TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(
        stemmed);
    List<String> tokenStringList = new ArrayList<>();
    for (KoreanTokenJava javaToken : javaTokens) {
//      LOGGER.debug((javaToken.toString());
//      if (KoreanPosJava.Noun.equals(javaToken.getPos()) ||
//          KoreanPosJava.ProperNoun.equals(javaToken.getPos()) ||
//          KoreanPosJava.Determiner.equals(javaToken.getPos()) ||
//          KoreanPosJava.Adjective.equals(javaToken.getPos()) ||
//          KoreanPosJava.Adverb.equals(javaToken.getPos()) ||
//          KoreanPosJava.Verb.equals(javaToken.getPos()) ||
//          KoreanPosJava.CashTag.equals(javaToken.getPos()) ||
//          KoreanPosJava.Email.equals(javaToken.getPos()) ||
//          KoreanPosJava.Exclamation.equals(javaToken.getPos()) ||
//          KoreanPosJava.ScreenName.equals(javaToken.getPos()) ||
//          KoreanPosJava.Hashtag.equals(javaToken.getPos())) {
      if (!KoreanPosJava.Josa.equals(javaToken.getPos()) &&
          !KoreanPosJava.Eomi.equals(javaToken.getPos()) &&
          !KoreanPosJava.PreEomi.equals(javaToken.getPos()) &&
          !KoreanPosJava.Conjunction.equals(javaToken.getPos()) &&
          !KoreanPosJava.NounPrefix.equals(javaToken.getPos()) &&
          !KoreanPosJava.VerbPrefix.equals(javaToken.getPos()) &&
          !KoreanPosJava.Suffix.equals(javaToken.getPos()) &&
          !KoreanPosJava.Punctuation.equals(javaToken.getPos()) &&
          !KoreanPosJava.Space.equals(javaToken.getPos())
      ){
//        LOGGER.debug(javaToken.toString());
        tokenStringList.add(javaToken.getText());
      }
    }
    return tokenStringList;
  }

}

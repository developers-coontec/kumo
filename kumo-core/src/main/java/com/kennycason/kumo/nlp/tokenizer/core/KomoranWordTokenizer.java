package com.kennycason.kumo.nlp.tokenizer.core;


import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.constant.SYMBOL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KomoranWordTokenizer implements WordTokenizer {

  private static final Logger LOGGER = LoggerFactory.getLogger(KomoranWordTokenizer.class);

  @Override
  public List<String> tokenize(String sentence) {

    Komoran komoran = new Komoran(DEFAULT_MODEL.STABLE);
//    komoran.setFWDic(Komoran.class.getClassLoader().getResourceAsStream("defaults/fwd.user"));
//    komoran.setUserDic(Komoran.class.getClassLoader().getResourceAsStream("defaults/dic.user"));
    KomoranResult analyzeResultList = komoran.analyze(sentence);
    List<String> tokenStringList = analyzeResultList.getMorphesByTags(Arrays.asList(
        SYMBOL.NNG,
        SYMBOL.NNP
//        SYMBOL.NNB,
//        SYMBOL.NP,
//        SYMBOL.NR,
//        SYMBOL.NOUN_SET,
//        SYMBOL.VV,
//        SYMBOL.VV,
//        SYMBOL.VV,
//        SYMBOL.VV,
//        SYMBOL.VV,
//        SYMBOL.VV,
        ));
//    List<String> tokenStringList = analyzeResultList.getMorphesByTags(SYMBOL.NOUN_SET);
//    List<Token> tokenList = analyzeResultList.getTokenList();
//    List<String> tokenStringList = new ArrayList<>();
//    for (Token oneToken : tokenList) {
//      LOGGER.debug(oneToken.toString());
//      LOGGER.debug((oneToken.toString());
//      tokenStringList.add(oneToken.getMorph());
//    }
//    for (String oneString : tokenStringList){
//      LOGGER.debug((oneString);
//    }
    return tokenStringList;
  }

}

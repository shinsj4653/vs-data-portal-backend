package visang.dataplatform.dataportal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CStdCheckServiceTest {

    @Test
    @DisplayName("표준단어조합 생성 테스트: SMS수신내용")
    void createStdWordCombTest4() {
        String input = "SMS수신내용";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("SMS_[수신]_CTNT");
    }

    CStdCheckService service = new CStdCheckService();

    @Test
    @DisplayName("표준단어조합 생성 테스트: 휴대전화번호")
    void createStdWordCombTest1() {
        String input = "휴대전화번호";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("SMPHN_NM");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: 학생휴대전화번호")
    void createStdWordCombTest2() {
        String input = "학생휴대전화번호";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("STDT_SMPHN_NM");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: 학교선생님휴대전화번호")
    void createStdWordCombTest3() {
        String input = "학교선생님휴대전화번호";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("SCHL_TCHR_SMPHN_NM");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: SMS차단목록")
    void createStdWordCombTest5() {
        String input = "SMS차단목록";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("SMS_[차단목록]");
    }

    @Test
    void join_메소드_예외처리_테스트() {
        String[] words = {"학생"};
        String result = String.join("_", words);

        assertThat(result).isEqualTo("학생");
    }

    @Test
    void ChatGPT_단어조합생성_결과확인() {
        List<String> results = List.of(
                "학생_전_화_번_호",
                "학_생_전_화번호",
                "학_생_전_화_번_호",
                "학_생_전_화_번호",
                "학생_전_화번호",
                "학_생전_화_번_호",
                "학_생_전화번호",
                "학생전_화번호",
                "학_생전화번호",
                "학생전_화_번_호",
                "학_생전_화번호",
                "학생전화_번호",
                "학_생_전_화번_호",
                "학_생_전화_번_호",
                "학_생_전화_번호",
                "학_생전_화번_호",
                "학생전_화_번호",
                "학생_전_화번_호",
                "학생전_화번_호",
                "학_생_전화번_호",
                "학생전화_번_호",
                "학생_전_화_번호",
                "학_생전화_번_호",
                "학생_전화번_호",
                "학_생전화_번호",
                "학생전화번호",
                "학생_전화_번_호",
                "학생_전화번호",
                "학생_전화_번호",
                "학_생전화번_호",
                "학_생전_화_번호",
                "학생전화번_호"
        );

        Map<Integer, List<String>> classifiedResults = new HashMap<>();

        for (String result : results) {
            String[] words = result.split("_");
            classifiedResults.putIfAbsent(words.length, new ArrayList<>());
            classifiedResults.get(words.length).add(result);
        }

        for (Map.Entry<Integer, List<String>> entry : classifiedResults.entrySet()) {
            System.out.println("========== 단어개수 : " + entry.getKey() + " ==========");
            for (String result : entry.getValue()) {
                System.out.println(result);
            }
        }
    }

    @Test
    void split_메소드_예외처리_테스트() {
        final String 학생전화번호 = "학생전화번호";
        String[] words = 학생전화번호.split("_");

        assertAll(
                () -> assertThat(words.length).isEqualTo(1),
                () -> assertThat(words[0]).isEqualTo(학생전화번호)
        );
    }

    class CStdCheckService {
        Map<String, String> dict = Map.of("휴대전화", "SMPHN",
                "전화", "PHN",
                "번호", "NM",
                "전화번호", "PHN_NM",
                "학생", "STDT",
                "학교", "SCHL",
                "선생님", "TCHR",
                "SMS", "SMS",
                "내용", "CTNT");

        private void generateCombinations(String remaining, String current, Set<String> result) {
            int length = remaining.length();

            if (length == 0) {
                result.add(current);
                return;
            }

            for (int i = 0; i < length; i++) {
                String prefix = remaining.substring(0, i + 1);
                String suffix = remaining.substring(i + 1);

                if (!current.isEmpty()) {
                    generateCombinations(suffix, current + "_" + prefix, result);
                } else {
                    generateCombinations(suffix, prefix, result);
                }
            }
        }

        // 가능한 모든 단어조합 생성
        private Set<String> generateWordCombinations(String input) {
            Set<String> result = new HashSet<>();
            generateCombinations(input, "", result);
            return result;
        }

        // 표준단어조합 생성
        public List<String> generateStdWordCombinations(String input) {
            List<WordCombination> candidates = new ArrayList<>();

            Set<String> wordCombinations = generateWordCombinations(input);
            for (String combination : wordCombinations) {
                String[] words = combination.split("_");
                int correctCount = 0; // 표준단어로 변환할 수 있는 단어 개수
                int wrongCount = 0; // 표준단어로 변환할 수 없는 단어 개수
                for (int i = 0; i < words.length; i++) {
                    if (dict.containsKey(words[i])) {
                        correctCount++;
                        words[i] = dict.get(words[i]);
                    } else {
                        wrongCount++;
                        words[i] = "[" + words[i] + "]";
                    }
                }

                String converted = String.join("_", words);
                candidates.add(new WordCombination(correctCount, wrongCount, converted));
            }

            Collections.sort(candidates);
            final WordCombination first = candidates.get(0);

            return candidates.stream()
                    .filter(c -> c.getWrongCount() == first.getWrongCount() && c.getCorrectCount() == first.getCorrectCount())
                    .map(WordCombination::getWordCombination)
                    .collect(Collectors.toList());
        }

        class WordCombination implements Comparable<WordCombination> {
            int correctCount;
            int wrongCount;
            String wordCombination;

            public WordCombination(int correctCount, int wrongCount, String wordCombination) {
                this.correctCount = correctCount;
                this.wrongCount = wrongCount;
                this.wordCombination = wordCombination;
            }

            @Override
            public int compareTo(WordCombination o) {
                if (this.wrongCount == o.wrongCount) {
                    return o.correctCount - this.correctCount;
                }
                return this.wrongCount - o.wrongCount;
            }

            public int getCorrectCount() {
                return correctCount;
            }

            public int getWrongCount() {
                return wrongCount;
            }

            public String getWordCombination() {
                return wordCombination;
            }
        }
    }

}
package visang.dataplatform.dataportal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CStdCheckServiceTest {

    public class WordCombinationsGenerator {
        Set<String> generateWordCombinations(String inputStr) {
            Set<String> result = new HashSet<>();
            generateCombinations(inputStr, "", result);
            return result;
        }

        void generateCombinations(String remaining, String current, Set<String> result) {
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
    @DisplayName("표준단어조합 생성 테스트: SMS수신내용")
    void createStdWordCombTest4() {
        String input = "SMS수신내용";
        List<String> results = service.generateStdWordCombinations(input);
//        Assertions.assertThat(result).isEqualTo("SMS_[수신]_CTNT");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: SMS차단목록")
    void createStdWordCombTest5() {
        String input = "SMS차단목록";
//        List<String> results = service.createStdWordCombs(input);
//        Assertions.assertThat(result).isEqualTo("SMS_[차단목록]");
    }

    @Test
    void ChatGPT_단어조합생성_테스트() {
        String inputStr = "학생전화번호";
        WordCombinationsGenerator generator = new WordCombinationsGenerator();
        // 단어 조합 생성
        Set<String> wordCombinations = generator.generateWordCombinations(inputStr);

        // 결과 출력
        for (String word : wordCombinations) {
            System.out.println(word);
        }
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

    @Test
    void join_메소드_예외처리_테스트() {
        String[] words = {"학생"};
        String result = String.join("_", words);
        assertThat(result).isEqualTo("학생");
    }

    class CStdCheckService {
        // 표준사전
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
        List<String> generateStdWordCombinations(String input) {
            Map<Integer, List<String>> classifiedResults = new HashMap<>();

            Set<String> wordCombinations = generateWordCombinations(input);
            for (String combination : wordCombinations) {
                String[] words = combination.split("_");
                int count = 0; // 표준단어로 변환할 수 없는 단어 개수
                for (int i = 0; i < words.length; i++) {
                    if (dict.containsKey(words[i])) {
                        words[i] = dict.get(words[i]);
                    } else {
                        count++;
                        words[i] = "[" + words[i] + "]";
                    }
                }

                String converted = String.join("_", words);
                classifiedResults.putIfAbsent(count, new ArrayList<>());
                classifiedResults.get(count).add(converted);
            }

            final Integer resultKey = classifiedResults.keySet()
                    .stream()
                    .sorted()
                    .collect(Collectors.toList())
                    .get(0);


            System.out.println("========== 표준단어조합 생성결과 ==========");
            for (String result : classifiedResults.get(resultKey)) {
                System.out.println(result);
            }

            return classifiedResults.get(resultKey);
        }
    }
}
package visang.dataplatform.dataportal.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CStdCheckServiceTest {

    CStdCheckService service = new CStdCheckService();

    @Test
    @DisplayName("표준단어조합 생성 테스트: 휴대전화번호")
    void createStdWordCombTest1() {
        String input = "휴대전화번호";
        List<String> results = service.createStdWordCombs(input);
//        Assertions.assertThat(results.size()).isEqualTo(1);
        Assertions.assertThat(results).containsOnly("SMPHN_NM");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: 학생휴대전화번호")
    void createStdWordCombTest2() {
        String input = "학생휴대전화번호";
        List<String> results = service.createStdWordCombs(input);
//        Assertions.assertThat(result).isEqualTo("STDT_SMPHN_NM");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: 학교선생님휴대전화번호")
    void createStdWordCombTest3() {
        String input = "학교선생님휴대전화번호";
        List<String> results = service.createStdWordCombs(input);
//        Assertions.assertThat(result).isEqualTo("SCHL_TCHR_SMPHN_NM");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: SMS수신내용")
    void createStdWordCombTest4() {
        String input = "SMS수신내용";
        List<String> results = service.createStdWordCombs(input);
//        Assertions.assertThat(result).isEqualTo("SMS_[수신]_CTNT");
    }

    @Test
    @DisplayName("표준단어조합 생성 테스트: SMS차단목록")
    void createStdWordCombTest5() {
        String input = "SMS차단목록";
        List<String> results = service.createStdWordCombs(input);
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

        // 표준단어조합 생성
        private void printResults(List<List<String>> candidates) {
            for (int i = 2; i >= 0; i--) {
                if (candidates.get(i).size() > 0) {
                    System.out.println("========== score: " + i + " ==========");
                    for (String result : candidates.get(i)) {
                        System.out.println(result);
                    }
                }
            }
        }

        List<String> createStdWordCombs(String input) {

            List<List<String>> candidates = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                candidates.add(new ArrayList<>());
            }

            // input가 표준용어, 표준단어인지 확인
            if (dict.containsKey(input)) {
                return List.of(dict.get(input));
            }

            for (int i = 1; i <= input.length() - 1; i++) {
                // left, right로 쪼갬
                String left = input.substring(0, i);
                String right = input.substring(i);

                // left right가 사전에 있으면 사전에서 가져와서 변환
                // 없으면 []로 감싸기
                int score = 0;
                if (dict.containsKey(left)) {
                    score++;
                    left = dict.get(left);
                } else {
                    left = "[" + left + "]";
                }

                if (dict.containsKey(right)) {
                    score++;
                    right = dict.get(right);
                } else {
                    right = "[" + right + "]";
                }

                // score 측정 0, 1, 2
                // 반환후보리스트에 추가
                candidates.get(score).add(left + "_" + right);
            }

            // 표준단어조합 생성 결과확인
            printResults(candidates);

            // 최고 score 측정
            // 반환후보리스트에서 최고 score를 가진 것들을 반환
            for (int i = 2; i > 0; i--) {
                if (candidates.get(i).size() > 0) {
                    return candidates.get(i);
                }
            }

            return List.of("[" + input + "]");
        }
        // 나머지 단어를 재귀처리하기 위한 메소드

        List<String> createBestStdWordCombs(String input) {

            List<List<String>> candidates = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                candidates.add(new ArrayList<>());
            }

            // input가 표준용어, 표준단어인지 확인
            if (dict.containsKey(input)) {
                return List.of(dict.get(input));
            }

            for (int i = 1; i <= input.length() - 1; i++) {
                // left, right로 쪼갬
                String left = input.substring(0, i);
                String right = input.substring(i);

                // left right가 사전에 있으면 사전에서 가져와서 변환
                // 없으면 []로 감싸기
                int score = 0;
                if (dict.containsKey(left)) {
                    score++;
                    left = dict.get(left);
                } else {
                    left = "[" + left + "]";
                }

                if (dict.containsKey(right)) {
                    score++;
                    right = dict.get(right);
                } else {
                    right = "[" + right + "]";
                }

                // score 측정 0, 1, 2
                // 반환후보리스트에 추가
                candidates.get(score).add(left + "_" + right);
            }

            // 최고 score 측정
            // 반환후보리스트에서 최고 score를 가진 것들을 반환
            for (int i = 2; i > 0; i--) {
                if (candidates.get(i).size() > 0) {
                    return candidates.get(i);
                }
            }

            return List.of("[" + input + "]");
        }


    }
}
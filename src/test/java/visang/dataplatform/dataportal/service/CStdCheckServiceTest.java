package visang.dataplatform.dataportal.service;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// TODO: "전화", "번호", "전화번호"가 모두 표준사전에 존재하는 경우
// "학생_전화_번호" "학생_전화번호"중 후자가 답이 되어야 함
// 이 부분을 고려하여 로직을 수정

public class CStdCheckServiceTest {

    @Test
    void SMS수신내용_표준단어조합_생성_테스트() {
        String input = "SMS수신내용";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("SMS_[수신]_CTNT");
    }

    CStdCheckService service = new CStdCheckService();

    @Test
    void 휴대전화번호_표준단어조합_생성_테스트() {
        String input = "휴대전화번호";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("SMPHN_NM");
    }

    @Test
    void 학생휴대전화번호_표준단어조합_생성_테스트() {
        String input = "학생휴대전화번호";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("STDT_SMPHN_NM");
    }

    @Test
    void 학교선생님휴대전화번호_표준단어조합_생성_테스트() {
        String input = "학교선생님휴대전화번호";
        List<String> results = service.generateStdWordCombinations(input);
        assertThat(results).containsOnly("SCHL_TCHR_SMPHN_NM");
    }

    @Test
    void SMS차단목록_표준단어조합_생성_테스트() {
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
        Map<String, String> dict = Map.of(
                "휴대전화", "SMPHN",
                "전화", "PHN",
                "번호", "NM",
                "전화번호", "PHN_NM",
                "학생", "STDT",
                "학교", "SCHL",
                "선생님", "TCHR",
                "SMS", "SMS",
                "내용", "CTNT"
        );

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

                String converted = String.join("_", words); // 표준단어조합 생성
                candidates.add(new WordCombination(correctCount, wrongCount, converted));
            }

            // 표준단어변환 실패 횟수 오름차순, 표준단어변환 성공 횟수 내림차순
            Collections.sort(candidates);  // 단어조합 정렬
            final WordCombination first = candidates.get(0); // 우선순위가 가장 놓은 단어조합 추출

            // 동일한 조건의 단어조합이 여러개 존재하는 경우 고려
            return candidates.stream()
                    .filter(c -> c.getWrongCount() == first.getWrongCount() && c.getCorrectCount() == first.getCorrectCount())
                    .map(WordCombination::getWordCombination)
                    .collect(Collectors.toList());
        }

        class WordCombination implements Comparable<WordCombination> {
            int correctCount; // 표준단어로 변환할 수 있는 단어 개수
            int wrongCount; // 표준단어로 변환할 수 없는 단어 개수
            String wordCombination; // 표준단어조합

            public WordCombination(int correctCount, int wrongCount, String wordCombination) {
                this.correctCount = correctCount;
                this.wrongCount = wrongCount;
                this.wordCombination = wordCombination;
            }

            @Override
            public int compareTo(WordCombination o) {
                // 표준단어변환 실패 횟수 오름차순
                // 표준단어변환 성공 횟수 내림차순
                // 표준단어변환에 가장 적게 실패하면서 가장 많이 성공한 단어조합이 우선순위가 높음

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
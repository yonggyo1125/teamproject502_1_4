package org.hidog.global.validators;

public interface PasswordValidator {
    /**
     * @param password
     * @param caseInsensitive false : 대 소문자 각각 1개씩 이상 포함 , true - 대소문자 구분 X
     * @return
     */
    default public boolean alphaCheck(String password, boolean caseInsensitive) {
        if (caseInsensitive) {//대소문자 구분없이 알파벳 체크
            return password.matches(".*[a-zA-Z]+.*");
        }
        return password.matches(".*[a-z]+.*") && password.matches(".*[A-Z]+.*");
    }

    ;

    /**
     * 숫자 복잡성 체크
     *
     * @param password
     * @return
     */
    default boolean numberCheck(String password) {
        return password.matches(".*\\d+.*");
    }

    /**
     * @param password
     * @return
     */
    default boolean specialCharsCheck(String password) {
        String pattern = ".*[^0-9a-zA-Zㄱ-ㅎ가-힣]+.*";
        return password.matches(pattern);
    }
}

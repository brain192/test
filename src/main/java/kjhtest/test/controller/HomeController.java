package kjhtest.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HomeController {
    @GetMapping("/test")
    public String home() {
        return "home";
    }

    @GetMapping("/cal1")
    public String cal1() {
        return "cal1";
    }

    @GetMapping("/cal1_ok")
    public String cal1_ok(@RequestParam("cal1") double cal1, @RequestParam("cal2") double cal2,
                          @RequestParam("see") String see, Model model) {


        double cal3;

        String a="test";


        if(Objects.equals(see, "a")) {
            cal3=cal1+cal2;
            a="no";
        }else if(Objects.equals(see, "b")){
            cal3=cal1-cal2;
        }else if(Objects.equals(see, "c")){
            cal3=cal1*cal2;
        }else if(Objects.equals(see, "d")){
            cal3=cal1/cal2;
        }else{
            cal3=0;
        }
        System.out.println(cal1);
        System.out.println(cal2);
        System.out.println(see);
        System.out.println(cal3);
        System.out.println(a);

        model.addAttribute("cal3",cal3);

        return "cal1_ok";
    }

    @GetMapping("/cal2")
    public String cal2() {
        return "cal2";
    }

    @GetMapping("/cal2_ok")
    public String cal2_ok(@RequestParam("see") String see, Model model) {

        model.addAttribute("see",see);

        System.out.println(see);

        Stack<String> s = new Stack<>();
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("\\d+|\\D");
        Matcher m = p.matcher(see);

        while (m.find()) {
            String split = m.group();
            list.add(split);
        }

        for (int i = 0; i < list.size(); i++) {
            if (!s.isEmpty() && s.peek().equals("*")) {
                // * 연산자 제거
                s.pop();

                // 계산할 수 가져오기
                String pop = s.pop();

                // 곱셈 계산 후 다시 스택에 입력
                s.push(String.valueOf(Integer.parseInt(pop) * Integer.parseInt(list.get(i))));
            } else {
                s.push(list.get(i));
            }
        }

        int result = Integer.parseInt(s.get(0));

        for (int i = 1; i < s.size(); i += 2) {
            String op = s.get(i);
            switch (op) {
                case "+":
                    result += Integer.parseInt(s.get(i + 1));
                    break;
                case "-":
                    result -= Integer.parseInt(s.get(i + 1));
            }
        }

        System.out.println(result);

        model.addAttribute("result",result);

        return "cal2_ok";
    }
}
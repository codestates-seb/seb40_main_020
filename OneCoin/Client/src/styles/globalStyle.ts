import { createGlobalStyle } from 'styled-components';
import resetCSS from 'styled-reset';

const GlobalStyle = createGlobalStyle`
 ${resetCSS}
 
  body {
    font-family: 'Pretendard Std', sans-serif;
    --gray: hsl(210,8%,45%);
    --lightgray: hsl(210,8%,75%);
    --yellow: #F5D042;
    --red: #E35047;
    --blue: #1660CC;
    --contentBg: #fafafa;
    --borderColor: #d9d9d9;
    --red: #E92222;
    --blue: #123EDB;
    background-color: var(--contentBg);
  }
  body.dark {
    -webkit-font-smoothing: antialiased;
    --gray: hsl(210,8%,45%);
    --lightgray: hsl(210,8%,75%);
    --yellow: #F5D042;
    --contentBg: #fafafa;
    --borderColor: #d9d9d9;
    background-color: var(--contentBg);
  }
`;

export default GlobalStyle;

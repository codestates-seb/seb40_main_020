import { createGlobalStyle } from 'styled-components';
import resetCSS from 'styled-reset';

const GlobalStyle = createGlobalStyle`
 ${resetCSS}
 *{
    margin: 0;
    padding: 0;
 }
 
  body {
    font-family: 'Pretendard Std', sans-serif;
    color: #000;
    margin: 0;
    --gray: hsl(210,8%,45%);
    --lightgray: hsl(210,8%,75%);
    --yellow: #F5D042;
    --contentBg: #fafafa;
    --borderColor: #d9d9d9;
    
    background-color: var(--bg);
    a, a.logo {
      color: hsl(210deg 8% 35%);
    }
  }
  body.dark {
    -webkit-font-smoothing: antialiased;
    --bg: rgba(14, 17, 22);
    --textNormal: rgba(255, 255, 255, 0.88);
    --textTitle: white;
    --textLink: yellow;
    --hr: hsla(0, 0%, 100%, 0.2);
    --hoverColor: #333;
    --footer-background-color: hsl(210,8%,15%);
    background-color: var(--bg);
    color: var(--textTitle);
    a, a.logo {
      color: white;
    }
    li {
      border-color: gray;
    }
  }
  a {
    text-decoration: none;
  }
`;

export default GlobalStyle;

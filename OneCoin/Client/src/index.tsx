import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import GlobalStyle from 'styles/globalStyle';
import { BrowserRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

const root = ReactDOM.createRoot(
	document.getElementById('root') as HTMLElement
);
root.render(
	// <React.StrictMode>
	<RecoilRoot>
		<BrowserRouter>
			<GlobalStyle />
			<App />
		</BrowserRouter>
	</RecoilRoot>
	// </React.StrictMode>
);

import styled from 'styled-components';
export const MainTopContainer = styled.div`
	width: 100%;
	display: flex;
	background-color: #fbfbef;
`;
export const ContentCotainer = styled.div`
	position: relative;
	margin-top: 40px;
	margin-left: 150px;
	img {
		width: 200px;
		display: block;
		margin-left: 16%;
		margin-top: -4%;
	}
	.btnContainer {
		display: flex;
		margin-top: 2em;
	}
`;
export const ServiceTitle = styled.div`
	width: 640px;
	height: 100px;
	margin-top: 12px;
	font-size: 42px;
	line-height: 1.4em;
	color: #292a0a;
	font-weight: bold;
	em {
		color: #aeb404;
	}
`;
export const SignupBtn = styled.button`
	width: 240px;
	height: 60px;
	display: block;
	border: none;
	border-radius: 30px;
	background-color: #aeb404;
	color: whitesmoke;
	font-size: x-large;
	margin-top: 10px;
`;

import styled, { css } from 'styled-components';
import { flexCenter } from '../../styles';

const backStyles = css`
	border: 1px solid var(--borderColor);
	background: #fff;
	min-height: 350px;
	padding: 30px;
	box-sizing: border-box;
`;

export const Wrapper = styled.div`
	${flexCenter}
	align-items: flex-start;
	transform: translateY(32%);
	.side-menu {
		${backStyles}
		width: 200px;
		li {
			margin-bottom: 25px;
			cursor: pointer;
			:first-child {
				font-weight: 700;
				color: var(--yellow);
			}
		}
	}

	.content-wrapper {
		${backStyles}
		width: 450px;
		border-left: 0 none;
	}

	.content-wrapper .profile-box {
		display: flex;
		margin-bottom: 30px;
		img {
			width: 80px;
			height: 80px;
			background: #ddd;
			border-radius: 50%;
		}
		ul {
			margin-left: 30px;
			display: flex;
			flex-direction: column;
			justify-content: center;
			li {
				margin: 3px 0;
			}
		}
	}

	.content-wrapper button {
		margin-top: 10px;
		/* display: block; */
		/* width: 180px; */
		height: 35px;
		border: 0 none;
		margin-left: 10px;
		background: var(--yellow);
		font-weight: 700;
		font-size: 1rem;
		color: #fff;
	}

	.content-wrapper .input-box {
		margin: 10px 0;
		span {
			display: inline-block;
			font-size: 0.9rem;
			width: 90px;
		}
		input {
			/* width: 180px; */
			height: 30px;
			border: 1px solid var(--borderColor);
			padding-left: 10px;
		}
		p {
			margin: 10px 0 0 90px;
			font-size: 0.8rem;
		}
	}
`;

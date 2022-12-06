import styled from 'styled-components';
import { flexRowBetween } from 'styles';

export const Wrapper = styled.div`
	.inner-wrap {
		${flexRowBetween}
		padding: 35px 30px;
		border-bottom: 1px solid var(--borderColor);
		input {
			width: 250px;
			height: 30px;
			padding-right: 10px;
			text-align: right;
			border: 1px solid var(--borderColor);
		}
	}

	.content-wrap {
		margin: 30px;
		p {
			font-size: 0.9rem;
		}
	}

	button {
		display: block;
		width: 90%;
		height: 50px;
		border: 0 none;
		margin: 0 auto;
		background: var(--yellow);
		font-weight: 700;
		font-size: 1rem;
		color: #fff;
	}
`;

import styled from 'styled-components';
import { container } from '../../styles';

export const FooterComponent = styled.footer`
	width: 100%;
	height: 130px;
	display: flex;
	justify-content: center;
	background: #ededed;
	.content {
		${container}
		display: flex;
		align-items: center;
		justify-content: space-between;
		.rep {
			display: flex;
			align-items: center;
			> div {
				:nth-child(1) {
					padding: 0 0.5rem;
				}
			}
		}
		a {
			display: flex;
			justify-content: center;
			padding: 0.125rem 0.125rem;
			align-items: center;
			text-decoration: none;
			margin: 0.125rem 0.25rem;
			color: #333;
			:active,
			:link,
			:visited {
				color: #333;
			}
		}
		img {
			width: 115px;
			height: 80px;
		}
		> div {
			:nth-child(1) {
				display: flex;
				align-items: center;
				justify-content: center;
			}
			> div {
				margin: 0.5rem 0;
			}
		}
	}
`;

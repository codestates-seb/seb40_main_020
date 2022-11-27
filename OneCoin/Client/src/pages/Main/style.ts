import styled, { css, keyframes } from 'styled-components';
import { flexCenter } from '../../styles/index';

const fadeIn = keyframes`
from {
	transform: translateX(-200px);
	opacity:0
}
to {
	transform: translateX(0px);
	opacity:1
}`;

const animate = css`
	opacity: 0;
	animation: ${fadeIn} 1s ease;
	animation-fill-mode: forwards;
`;
export const Wrap = styled.div`
	width: 100%;
	.slick-dots {
		position: absolute;
		bottom: 100px;
		width: 100%;
		text-align: center;
	}
	.slick-dots li {
		display: inline-block;
	}
	.slick-dots li button {
		font-size: 0;
		margin: 0 15px;
		width: 15px;
		height: 15px;
		background: #fff;
		border: 1px solid var(--borderColor);
		border-radius: 50%;
		padding: 0;
		cursor: pointer;
	}
	.slick-dots li.slick-active button {
		background: var(--yellow);
	}

	.slick-slide.slick-active {
		${animate}
		h2 {
			${animate}
			animation-delay: 0.5s;
		}
		p {
			${animate}
			&:nth-of-type(1) {
				animation-delay: 1s;
			}
			&:nth-of-type(2) {
				animation-delay: 1.5s;
			}
		}
		.button {
			${animate}
			animation-delay: 2s;
		}
	}
`;

export const SlideWrap = styled.div`
	margin: 50px auto 100px;
`;
export const SlideInner = styled.div`
	${flexCenter}
	width: 1000px;
	margin: 0 auto;
	justify-content: space-between;
`;
export const TextWrap = styled.div`
	h2 {
		font-size: 1.8rem;
		font-weight: 700;
		span {
			color: var(--yellow);
		}
	}
	p {
		font-size: 1.1rem;
		line-height: normal;
		margin-top: 30px;
	}
	.button {
		display: block;
		margin-top: 20px;
		width: 150px;
		padding: 15px 0 12px 0;
		border: 0 none;
		background: var(--yellow);
		text-align: center;
		text-decoration: none;
		color: #fff;
		:hover {
			background: #f0c000;
		}
	}
`;

import React from 'react';
import { render, screen } from '@testing-library/react';
import VerifyAuth from '../main/VerifyAuth';

test('renders learn react link', () => {
  render(<VerifyAuth />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});

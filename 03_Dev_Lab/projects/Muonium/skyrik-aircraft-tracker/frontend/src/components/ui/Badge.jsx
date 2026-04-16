import { cn } from '../../lib/utils';

const badgeVariants = {
  default: 'border border-gray-200 bg-white text-gray-950',
  primary: 'bg-blue-600 text-white',
  success: 'bg-green-600 text-white',
  warning: 'bg-yellow-600 text-white',
  danger: 'bg-red-600 text-white',
  secondary: 'bg-gray-200 text-gray-900',
};

export function Badge({ variant = 'default', className, ...props }) {
  return (
    <div
      className={cn(
        'inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold transition-colors',
        badgeVariants[variant],
        className
      )}
      {...props}
    />
  );
}
